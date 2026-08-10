package com.qst.medical.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class AiKnowledgeService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.model}")
    private String model;

    private static final int MAX_RESULTS = 20;

    private static final String TABLE_SCHEMAS = """
        数据库各表结构如下：

        account (id BIGINT, realname VARCHAR, uname VARCHAR, pwd VARCHAR, phonenumber VARCHAR, utype VARCHAR, updatetime DATETIME, createtime DATETIME)
        -- utype: '1'管理员, '2'医生, '3'患者

        doctor (id BIGINT, account_id BIGINT, age INT, sex INT, hospital VARCHAR, level_id BIGINT, type_id BIGINT, createtime DATETIME)
        -- sex: 1男, 2女; 关联 account.id; 关联 doctor_level.id; 关联 treat_type.id

        doctor_level (id BIGINT, name VARCHAR)
        -- 医师级别: 1主任医师, 2副主任医师, 3主治医师, 4住院医师, 5实习医师

        patient (id INT, pname VARCHAR, age INT, sex INT, state INT, enter_time DATETIME, out_time DATETIME)
        -- sex: 1男, 2女; state: 0已出院, 1在院

        drug (drug_id BIGINT, drug_name VARCHAR, drug_info TEXT, drug_effect TEXT, publisher VARCHAR, createtime DATETIME, updatetime DATETIME)

        drugcompany (company_id BIGINT, company_name VARCHAR, company_phone VARCHAR, createtime DATETIME, updatetime DATETIME)

        sale (sale_id BIGINT, sale_name VARCHAR, sale_phone VARCHAR, longitude DOUBLE, latitude DOUBLE, address VARCHAR, createtime DATETIME, updatetime DATETIME)

        drug_sale (id BIGINT, drug_id BIGINT, sale_id BIGINT)
        -- 关联 drug.drug_id, 关联 sale.sale_id

        medical_policy (id BIGINT, title VARCHAR, message TEXT, city_id BIGINT, create_time VARCHAR, update_time VARCHAR)
        -- 关联 city.city_id; city_id 外键指向 city 表

        city (city_id BIGINT, city_number INT, province VARCHAR, city VARCHAR, createtime DATETIME, updatetime DATETIME)
        -- province 存省/直辖市名(如"北京市"), city 存区/市名(如"朝阳区"); 查城市用 province 或 city 均可

        company_policy (id BIGINT, title VARCHAR, message TEXT, company_id BIGINT, create_time DATETIME, update_time DATETIME)
        -- 关联 drugcompany.company_id

        material (id INT, title VARCHAR, message TEXT, create_time DATETIME, update_time DATETIME)

        treat_type (id BIGINT, name VARCHAR)
        -- 诊治类型: 1门诊, 2急诊, 3住院, 4日间手术, 5家庭医生签约服务, 6互联网诊疗
        """;

    private static final Set<String> DANGEROUS_KEYWORDS = Set.of(
        "DROP", "DELETE", "UPDATE", "INSERT", "ALTER", "CREATE", "TRUNCATE",
        "RENAME", "REPLACE", "EXEC", "EXECUTE", "MERGE", "CALL", "LOAD",
        "IMPORT", "GRANT", "REVOKE", "INTO OUTFILE", "INTO DUMPFILE",
        "BENCHMARK", "SLEEP", "WAITFOR"
    );

    public Map<String, Object> query(String question) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("question", question);

        String sql = generateSql(question);
        result.put("generatedSql", sql);

        if ("UNABLE_TO_QUERY".equals(sql)) {
            result.put("answer", freeChat(question));
            result.put("generatedSql", "N/A");
            return result;
        }

        if (!isSqlSafe(sql)) {
            result.put("answer", freeChat(question));
            result.put("generatedSql", "N/A");
            return result;
        }

        try {
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
            System.out.println("[AiKnowledge] SQL执行成功，查到 " + data.size() + " 条数据");
            result.put("data", data);
            if (data.isEmpty()) {
                result.put("answer", freeChat(question));
                result.put("generatedSql", sql);
            } else {
                String answer = formatAnswer(question, sql, data);
                System.out.println("[AiKnowledge] formatAnswer返回: " + answer.substring(0, Math.min(100, answer.length())) + "...");
                result.put("answer", answer);
            }
        } catch (Exception e) {
            System.err.println("[AiKnowledge] 查询/格式化失败: " + e.getMessage());
            e.printStackTrace();
            result.put("answer", freeChat(question));
            result.put("generatedSql", "N/A");
        }

        return result;
    }

    private String generateSql(String question) {
        String systemPrompt = "你是一个医疗数据库查询助手。根据用户的问题，生成一条MySQL SELECT查询语句。\n\n" +
            TABLE_SCHEMAS + "\n" +
            "要求：\n" +
            "1. 只输出SELECT语句，不要有任何解释、标记或代码块格式\n" +
            "2. 如果问题无法用SQL回答，只输出 UNABLE_TO_QUERY\n" +
            "3. 模糊搜索时使用 LIKE '%关键词%'\n" +
            "4. 查询结果限制在" + MAX_RESULTS + "条以内\n" +
            "5. 关联查询时使用JOIN\n" +
            "6. 不要使用中文作为列名，只使用英文列名";

        String response = callDeepSeek(systemPrompt, question);
        System.out.println("[AiKnowledge] DeepSeek原始返回: " + response);
        String cleaned = cleanSql(response);
        System.out.println("[AiKnowledge] 清理后SQL: " + cleaned);
        return cleaned;
    }

    private String formatAnswer(String question, String sql, List<Map<String, Object>> data) {
        String systemPrompt = "你是一个医疗信息助手。根据用户的问题和数据库查询结果，给出专业且有帮助的回答。\n" +
            "严格要求：\n" +
            "1. 先说明查询到了几条记录（必须与查询结果中的记录数一致，不得多报或少报）\n" +
            "2. 只陈述查询结果中已有的信息，绝对不要添加数据库中没有的数据\n" +
            "3. 如果涉及药品，可以说明其功效和适用场景，但必须附上免责声明\n" +
            "4. 如果涉及医生，可以说明其资历和所在医院\n" +
            "5. 如果涉及政策，可以简要说明政策要点\n" +
            "6. 回答需简洁明了，用适当的分段和列表提升可读性\n" +
            "7. 涉及用药建议时，结尾必须加上：⚠️ 以上建议基于数据库信息，实际用药请遵医嘱";

        String userMessage = "用户问题：" + question + "\n\n数据库查询结果（共" + data.size() + "条）：\n" + formatDataForPrompt(data);
        return callDeepSeek(systemPrompt, userMessage);
    }

    private String freeChat(String question) {
        String systemPrompt = "你是一个医疗信息助手小伊。你连接的数据库中暂时没有与用户问题直接相关的数据，但你可以基于你的医学知识给出一般性建议。\n" +
            "要求：\n" +
            "1. 开头说明：'数据库中没有直接相关的数据，以下为一般性建议：'\n" +
            "2. 给出专业但安全的信息\n" +
            "3. 涉及用药或诊断时，必须提醒用户咨询医生\n" +
            "4. 回答简洁明了\n" +
            "5. 结尾加上：⚠️ 以上为通用建议，不能替代专业医疗诊断，请及时就医";

        return callDeepSeek(systemPrompt, question);
    }

    private String callDeepSeek(String systemPrompt, String userMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", model);
            requestBody.put("max_tokens", 2048);
            requestBody.put("temperature", 0.1);
            requestBody.put("stream", false);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(apiUrl, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText().trim();
        } catch (Exception e) {
            throw new RuntimeException("AI接口调用失败：" + e.getMessage(), e);
        }
    }

    private String cleanSql(String response) {
        String sql = response.trim();
        sql = sql.replaceAll("```sql\\s*", "");
        sql = sql.replaceAll("```\\s*", "");
        sql = sql.replaceAll("[;\\s]+$", "");
        sql = sql.replaceAll("\n", " ");
        sql = sql.replaceAll("\\s+", " ");
        return sql.trim();
    }

    private boolean isSqlSafe(String sql) {
        String upper = sql.toUpperCase().trim();
        if (!upper.startsWith("SELECT")) {
            return false;
        }
        if (sql.contains(";")) {
            return false;
        }
        for (String keyword : DANGEROUS_KEYWORDS) {
            Pattern pattern = Pattern.compile("\\b" + keyword + "\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(sql).find()) {
                return false;
            }
        }
        return true;
    }

    private String formatDataForPrompt(List<Map<String, Object>> data) {
        StringBuilder sb = new StringBuilder();
        sb.append("共").append(data.size()).append("条记录：\n");
        int count = 0;
        for (Map<String, Object> row : data) {
            if (count++ >= MAX_RESULTS) break;
            sb.append(count).append(". ");
            row.forEach((k, v) -> sb.append(k).append("=").append(v).append(", "));
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        return sb.toString();
    }
}