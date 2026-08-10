package com.qst.medical.controller;

import com.qst.medical.common.Result;
import com.qst.medical.service.AiKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/knowledge")
public class AiKnowledgeController {

    @Autowired
    private AiKnowledgeService aiKnowledgeService;

    @PostMapping("/query")
    public Result<Map<String, Object>> query(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question == null || question.trim().isEmpty()) {
            return Result.error("问题不能为空");
        }
        try {
            Map<String, Object> result = aiKnowledgeService.query(question.trim());
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("知识库查询失败：" + e.getMessage());
        }
    }
}