/*
 Navicat Premium Data Transfer

 Source Server         : huiyiliao
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : smartdoctor

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 28/07/2026 14:16:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id（账号表）',
  `realname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `uname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `phonenumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话号码',
  `utype` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色类型：1管理员，2医生，3患者',
  `updatetime` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `createtime` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (7, '张三', 'zs', '$2a$10$txcqaNPRuEaXhgvZrHluOe572c0uL/QlMe0vATSWDoWAcLq360hwW', '13100000001', '3', NULL, '2026-07-28 10:58:43.000000');

-- ----------------------------
-- Table structure for china
-- ----------------------------
DROP TABLE IF EXISTS `china`;
CREATE TABLE `china`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '省/市/县id',
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省/市/县名',
  `parent_id` int NULL DEFAULT NULL COMMENT '上级省/市id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 713 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '中国省/市/县数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of china
-- ----------------------------
INSERT INTO `china` VALUES (1, '北京市', NULL);
INSERT INTO `china` VALUES (2, '上海市', NULL);
INSERT INTO `china` VALUES (3, '广东省', NULL);
INSERT INTO `china` VALUES (4, '浙江省', NULL);
INSERT INTO `china` VALUES (5, '江苏省', NULL);
INSERT INTO `china` VALUES (6, '湖北省', NULL);
INSERT INTO `china` VALUES (7, '四川省', NULL);
INSERT INTO `china` VALUES (11, '朝阳区', 1);
INSERT INTO `china` VALUES (12, '海淀区', 1);
INSERT INTO `china` VALUES (13, '东城区', 1);
INSERT INTO `china` VALUES (14, '西城区', 1);
INSERT INTO `china` VALUES (15, '丰台区', 1);
INSERT INTO `china` VALUES (21, '浦东新区', 2);
INSERT INTO `china` VALUES (22, '黄浦区', 2);
INSERT INTO `china` VALUES (23, '徐汇区', 2);
INSERT INTO `china` VALUES (24, '静安区', 2);
INSERT INTO `china` VALUES (31, '广州市', 3);
INSERT INTO `china` VALUES (32, '深圳市', 3);
INSERT INTO `china` VALUES (33, '东莞市', 3);
INSERT INTO `china` VALUES (34, '佛山市', 3);
INSERT INTO `china` VALUES (41, '杭州市', 4);
INSERT INTO `china` VALUES (42, '宁波市', 4);
INSERT INTO `china` VALUES (43, '温州市', 4);
INSERT INTO `china` VALUES (51, '南京市', 5);
INSERT INTO `china` VALUES (52, '苏州市', 5);
INSERT INTO `china` VALUES (53, '无锡市', 5);
INSERT INTO `china` VALUES (61, '武汉市', 6);
INSERT INTO `china` VALUES (62, '宜昌市', 6);
INSERT INTO `china` VALUES (71, '成都市', 7);
INSERT INTO `china` VALUES (72, '绵阳市', 7);
INSERT INTO `china` VALUES (311, '天河区', 31);
INSERT INTO `china` VALUES (312, '越秀区', 31);
INSERT INTO `china` VALUES (313, '海珠区', 31);
INSERT INTO `china` VALUES (321, '南山区', 32);
INSERT INTO `china` VALUES (322, '福田区', 32);
INSERT INTO `china` VALUES (323, '罗湖区', 32);
INSERT INTO `china` VALUES (411, '西湖区', 41);
INSERT INTO `china` VALUES (412, '滨江区', 41);
INSERT INTO `china` VALUES (413, '拱墅区', 41);
INSERT INTO `china` VALUES (611, '武昌区', 61);
INSERT INTO `china` VALUES (612, '洪山区', 61);
INSERT INTO `china` VALUES (711, '武侯区', 71);
INSERT INTO `china` VALUES (712, '锦江区', 71);

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`  (
  `city_id` bigint NOT NULL AUTO_INCREMENT COMMENT '城市编号',
  `city_number` int NULL DEFAULT NULL COMMENT '城市编号',
  `createtime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`city_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '城市地点管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES (1, 11, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (2, 12, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (3, 21, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (4, 22, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (5, 31, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (6, 32, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (7, 41, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (8, 51, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (9, 61, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `city` VALUES (10, 71, '2026-07-28 10:46:51', '2026-07-28 10:46:51');

-- ----------------------------
-- Table structure for company_policy
-- ----------------------------
DROP TABLE IF EXISTS `company_policy`;
CREATE TABLE `company_policy`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公司政策主键id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政策标题',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '政策内容',
  `company_id` bigint NULL DEFAULT NULL COMMENT '公司id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医药公司政策表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_policy
-- ----------------------------
INSERT INTO `company_policy` VALUES (1, '国药集团2025年药品质量管控新规', '为加强药品质量管控，国药集团自2025年1月起实施新版GMP质量管理体系，涵盖原料采购、生产加工、仓储运输全流程。', 1, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `company_policy` VALUES (2, '华润医药数字化转型战略', '华润医药2025年全面推进数字化转型，建设智慧制药工厂，实现生产全流程数据采集与追溯。', 2, '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `company_policy` VALUES (3, '上海医药集团集采中标药品供应保障方案', '上海医药集团承诺保障国家集采中标药品的稳定供应，储备量不低于3个月用量，确保市场不出现断供。', 3, '2026-07-28 10:46:51', '2026-07-28 10:46:51');

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id(医生信息表)',
  `account_id` bigint NULL DEFAULT NULL COMMENT '账号id',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `sex` int NULL DEFAULT NULL COMMENT '性别：1男，2女',
  `hospital` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属医院',
  `level_id` bigint NULL DEFAULT NULL COMMENT '医师级别id',
  `type_id` bigint NULL DEFAULT NULL COMMENT '诊治类型id',
  `createtime` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES (1, 2, 45, 1, '北京市朝阳医院', 1, 1, '2026-07-28 10:46:51.000000');
INSERT INTO `doctor` VALUES (2, 3, 38, 2, '上海市浦东医院', 2, 2, '2026-07-28 10:46:51.000000');
INSERT INTO `doctor` VALUES (3, 4, 32, 1, '广州市第一人民医院', 3, 3, '2026-07-28 10:46:51.000000');

-- ----------------------------
-- Table structure for doctor_level
-- ----------------------------
DROP TABLE IF EXISTS `doctor_level`;
CREATE TABLE `doctor_level`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '级别id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '级别名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医生级别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor_level
-- ----------------------------
INSERT INTO `doctor_level` VALUES (1, '主任医师');
INSERT INTO `doctor_level` VALUES (2, '副主任医师');
INSERT INTO `doctor_level` VALUES (3, '主治医师');
INSERT INTO `doctor_level` VALUES (4, '住院医师');
INSERT INTO `doctor_level` VALUES (5, '实习医师');

-- ----------------------------
-- Table structure for drug
-- ----------------------------
DROP TABLE IF EXISTS `drug`;
CREATE TABLE `drug`  (
  `drug_id` bigint NOT NULL AUTO_INCREMENT COMMENT '药品信息表id',
  `drug_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药名',
  `drug_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '药品成分信息',
  `drug_effect` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '药品功能',
  `drug_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药品图片url',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布者',
  `createtime` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`drug_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '药品信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of drug
-- ----------------------------
INSERT INTO `drug` VALUES (1, '阿莫西林胶囊', '主要成分为阿莫西林，属于青霉素类抗生素', '用于敏感菌引起的上呼吸道感染、泌尿系统感染等', NULL, '国药集团', '2026-07-28 10:46:51.000000', '2026-07-28 10:46:51.000000');
INSERT INTO `drug` VALUES (2, '布洛芬缓释胶囊', '主要成分为布洛芬，属于非甾体抗炎药', '用于缓解轻至中度疼痛，如头痛、关节痛、牙痛等', NULL, '华润医药', '2026-07-28 10:46:51.000000', '2026-07-28 10:46:51.000000');
INSERT INTO `drug` VALUES (3, '氨氯地平片', '主要成分为苯磺酸氨氯地平，属于钙通道阻滞剂', '用于治疗高血压和冠心病', NULL, '上海医药集团', '2026-07-28 10:46:51.000000', '2026-07-28 10:46:51.000000');
INSERT INTO `drug` VALUES (4, '二甲双胍片', '主要成分为盐酸二甲双胍', '用于治疗2型糖尿病，改善胰岛素抵抗', NULL, '广州医药集团', '2026-07-28 10:46:51.000000', '2026-07-28 10:46:51.000000');
INSERT INTO `drug` VALUES (5, '奥美拉唑肠溶胶囊', '主要成分为奥美拉唑，属于质子泵抑制剂', '用于治疗胃溃疡、十二指肠溃疡、反流性食管炎', NULL, '国药集团', '2026-07-28 10:46:51.000000', '2026-07-28 10:46:51.000000');

-- ----------------------------
-- Table structure for drug_sale
-- ----------------------------
DROP TABLE IF EXISTS `drug_sale`;
CREATE TABLE `drug_sale`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `drug_id` bigint NULL DEFAULT NULL COMMENT '药的名称',
  `sale_id` bigint NULL DEFAULT NULL COMMENT '售卖该药的药店的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '药品药店关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of drug_sale
-- ----------------------------
INSERT INTO `drug_sale` VALUES (1, 1, 1);
INSERT INTO `drug_sale` VALUES (2, 1, 2);
INSERT INTO `drug_sale` VALUES (3, 2, 1);
INSERT INTO `drug_sale` VALUES (4, 2, 3);
INSERT INTO `drug_sale` VALUES (5, 3, 2);
INSERT INTO `drug_sale` VALUES (6, 3, 4);
INSERT INTO `drug_sale` VALUES (7, 4, 3);
INSERT INTO `drug_sale` VALUES (8, 4, 5);
INSERT INTO `drug_sale` VALUES (9, 5, 1);
INSERT INTO `drug_sale` VALUES (10, 5, 4);

-- ----------------------------
-- Table structure for drugcompany
-- ----------------------------
DROP TABLE IF EXISTS `drugcompany`;
CREATE TABLE `drugcompany`  (
  `company_id` bigint NOT NULL AUTO_INCREMENT COMMENT '药品公司信息表id',
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名',
  `company_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司电话',
  `createtime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`company_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医药公司表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of drugcompany
-- ----------------------------
INSERT INTO `drugcompany` VALUES (1, '国药集团', '021-50806666', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `drugcompany` VALUES (2, '华润医药', '010-82845678', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `drugcompany` VALUES (3, '上海医药集团', '021-63730908', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `drugcompany` VALUES (4, '广州医药集团', '020-81218888', '2026-07-28 10:46:51', '2026-07-28 10:46:51');

-- ----------------------------
-- Table structure for material
-- ----------------------------
DROP TABLE IF EXISTS `material`;
CREATE TABLE `material`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '必备材料信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '必备材料表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of material
-- ----------------------------
INSERT INTO `material` VALUES (1, '医保报销必备材料清单', '1.本人身份证原件及复印件\n2.社会保障卡/医保电子凭证\n3.住院发票原件\n4.费用明细清单\n5.出院小结\n6.诊断证明书', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `material` VALUES (2, '异地就医备案所需材料', '1.身份证原件及复印件\n2.社会保障卡\n3.异地居住证明（居住证/暂住证）\n4.异地就医备案表（可在医保经办机构领取或网上下载）', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `material` VALUES (3, '门诊慢特病申请材料', '1.身份证原件及复印件\n2.社会保障卡\n3.二级及以上医院出具的诊断证明\n4.相关检查报告单\n5.门诊慢特病申请表', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `material` VALUES (4, '生育保险报销材料', '1.身份证原件及复印件\n2.社会保障卡\n3.出生医学证明\n4.生育服务证/准生证\n5.住院发票及费用清单', '2026-07-28 10:46:51', '2026-07-28 10:46:51');

-- ----------------------------
-- Table structure for medical_policy
-- ----------------------------
DROP TABLE IF EXISTS `medical_policy`;
CREATE TABLE `medical_policy`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '医保政策信息表id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '政策标题',
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简介',
  `city_id` bigint NULL DEFAULT NULL COMMENT '所属城市id',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '医保政策表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of medical_policy
-- ----------------------------
INSERT INTO `medical_policy` VALUES (1, '2025年度北京市朝阳区城乡居民基本医疗保险参保缴费通知', '2025年度城乡居民基本医疗保险个人缴费标准为：老年人每人每年400元，学生儿童每人每年375元，劳动年龄内居民每人每年705元。参保缴费期为2024年9月1日至2024年12月31日。', 1, '2024-09-01', '2024-09-01');
INSERT INTO `medical_policy` VALUES (2, '北京市海淀区职工医保门诊共济保障机制实施细则', '自2025年1月1日起，职工医保门诊统筹基金年度最高支付限额提高至20000元。在职职工在一级及以下医疗机构报销比例提高至80%，二级医疗机构70%，三级医疗机构60%。', 2, '2024-12-15', '2025-01-01');
INSERT INTO `medical_policy` VALUES (3, '上海市浦东新区长期护理保险试点扩面方案', '2025年起，浦东新区将长期护理保险试点范围扩大至全区所有街道。符合条件的失能老人可申请居家照护或机构照护服务，基金支付比例最高可达90%。', 3, '2025-01-15', '2025-02-01');
INSERT INTO `medical_policy` VALUES (4, '上海市黄浦区医保电子凭证推广应用工作方案', '全面推广医保电子凭证，实现\"一码通办\"。参保人可通过国家医保服务平台APP、微信、支付宝等渠道激活医保电子凭证，在定点医药机构实现扫码就医购药。', 4, '2025-02-01', '2025-03-01');
INSERT INTO `medical_policy` VALUES (5, '广州市2025年度职工基本医疗保险缴费基数调整通知', '2025年度广州市职工基本医疗保险缴费基数上限调整为33786元，下限调整为6757元。灵活就业人员参加职工医保的缴费基数按新标准执行。', 5, '2025-01-01', '2025-01-01');
INSERT INTO `medical_policy` VALUES (6, '深圳市异地就医直接结算服务扩面实施方案', '2025年深圳市将新增50家定点医疗机构开通跨省异地就医直接结算服务。异地安置退休人员、异地长期居住人员、常驻异地工作人员均可享受住院费用直接结算。', 6, '2025-03-01', '2025-03-15');
INSERT INTO `medical_policy` VALUES (7, '杭州市\"西湖益联保\"2025年度参保指南', '杭州市商业补充医疗保险\"西湖益联保\"2025年度保费为150元/人/年，保额最高可达315万元。参保不限年龄、不限职业、不限既往病史。', 7, '2024-10-15', '2024-11-01');
INSERT INTO `medical_policy` VALUES (8, '南京市门诊慢特病医疗保障管理办法', '自2025年起，南京市将高血压、糖尿病、恶性肿瘤门诊治疗等12种慢性病和特殊疾病纳入门诊慢特病管理。合规医疗费用报销比例不低于70%，年度最高支付限额根据病种分别为3000-80000元不等。', 8, '2025-01-01', '2025-01-01');
INSERT INTO `medical_policy` VALUES (9, '武汉市职工医保个人账户家庭共济实施细则', '2025年3月起，武汉市职工医保个人账户余额可用于支付配偶、父母、子女在定点医疗机构就医发生的个人负担费用，以及在定点零售药店购买药品、医疗器械的费用。', 9, '2025-02-20', '2025-03-01');
INSERT INTO `medical_policy` VALUES (10, '成都市大病医疗互助补充保险政策解读', '2025年度成都市大病医疗互助补充保险最高支付限额为40万元。参保人员在基本医疗保险报销后，个人自付合规费用超过1万元的部分，按50%-80%的比例分段报销。', 10, '2025-01-10', '2025-01-10');
INSERT INTO `medical_policy` VALUES (11, '北京市海淀区医保移动支付全覆盖实施方案', '海淀区全面推广医保移动支付，参保人无需携带实体社保卡，通过手机即可完成挂号、就诊、缴费、取药全流程。预计2025年6月底前覆盖全区所有二级及以上医疗机构。', 2, '2025-04-01', '2025-04-15');
INSERT INTO `medical_policy` VALUES (12, '深圳市生育保险和职工医保合并实施政策问答', '深圳市生育保险与职工基本医疗保险合并实施后，用人单位缴费比例为8.5%，个人缴费比例为2%。生育津贴按照职工所在用人单位上年度职工月平均工资计发。', 6, '2025-02-15', '2025-03-01');
INSERT INTO `medical_policy` VALUES (13, '广州市医保定点零售药店管理暂行办法', '明确医保定点零售药店的准入条件、服务协议管理、监督检查等要求。定点药店需配备至少1名执业药师，营业面积不低于80平方米，具备24小时售药服务能力。', 5, '2025-03-10', '2025-03-20');
INSERT INTO `medical_policy` VALUES (14, '杭州市DRG付费方式改革实施方案', '杭州市全面推进按疾病诊断相关分组(DRG)付费改革，2025年底前实现全市二级及以上医疗机构全覆盖。通过DRG付费引导医疗机构规范诊疗行为，控制医疗费用不合理增长。', 7, '2025-03-01', '2025-04-01');
INSERT INTO `medical_policy` VALUES (15, '成都市医保基金监管信用管理办法', '建立医保基金监管信用体系，对定点医药机构实行信用等级评价。信用等级分为A、B、C、D四级，评价结果与基金预算、稽核频次挂钩。对D级机构实施重点监管和联合惩戒。', 10, '2025-04-01', '2025-05-01');

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '患者id',
  `pname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '患者姓名',
  `age` int NULL DEFAULT NULL COMMENT '患者年龄',
  `sex` int NULL DEFAULT NULL COMMENT '性别：1男，2女',
  `state` int NULL DEFAULT NULL COMMENT '状态',
  `enter_time` datetime NULL DEFAULT NULL COMMENT '入院时间',
  `out_time` datetime NULL DEFAULT NULL COMMENT '出院时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '患者信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (1, '王小明', 35, 1, 0, '2025-01-10 09:30:00', '2025-01-17 14:00:00');
INSERT INTO `patient` VALUES (2, '李小红', 28, 2, 0, '2025-02-15 10:00:00', '2025-02-20 11:00:00');
INSERT INTO `patient` VALUES (3, '张大伟', 52, 1, 1, '2025-03-05 08:45:00', NULL);
INSERT INTO `patient` VALUES (4, '刘美玲', 41, 2, 0, '2025-03-12 15:20:00', '2025-03-18 09:00:00');
INSERT INTO `patient` VALUES (5, '陈建国', 67, 1, 1, '2025-04-01 11:00:00', NULL);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `pid` int NULL DEFAULT NULL COMMENT '父id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件',
  `level` int NULL DEFAULT NULL COMMENT '菜单级别',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, NULL, 'Home', '/home', 'Home', 1, '首页');
INSERT INTO `permission` VALUES (2, NULL, 'DrugCompany', '/drug/company', 'DrugCompany', 1, '医药公司管理');
INSERT INTO `permission` VALUES (3, NULL, 'SaleLocation', '/sale/location', 'SaleLocation', 1, '销售地点管理');
INSERT INTO `permission` VALUES (4, NULL, 'City', '/city', 'City', 1, '城市信息管理');
INSERT INTO `permission` VALUES (5, NULL, 'DrugList', '/drug/list', 'DrugList', 1, '药品信息管理');
INSERT INTO `permission` VALUES (6, NULL, 'PolicyList', '/policy/list', 'PolicyList', 1, '医保政策管理');
INSERT INTO `permission` VALUES (7, NULL, 'CompanyPolicy', '/company/policy', 'CompanyPolicy', 1, '医药公司政策管理');
INSERT INTO `permission` VALUES (8, NULL, 'Doctor', '/doctor', 'Doctor', 1, '医生信息管理');
INSERT INTO `permission` VALUES (9, NULL, 'Material', '/material', 'Material', 1, '必备材料管理');
INSERT INTO `permission` VALUES (10, NULL, 'SecurityLog', '/log', 'SecurityLog', 1, '安全日志管理');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `roleName` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色id',
  `per_id` int NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, '1', 1);
INSERT INTO `role_permission` VALUES (2, '1', 2);
INSERT INTO `role_permission` VALUES (3, '1', 3);
INSERT INTO `role_permission` VALUES (4, '1', 4);
INSERT INTO `role_permission` VALUES (5, '1', 5);
INSERT INTO `role_permission` VALUES (6, '1', 6);
INSERT INTO `role_permission` VALUES (7, '1', 7);
INSERT INTO `role_permission` VALUES (8, '1', 8);
INSERT INTO `role_permission` VALUES (9, '1', 9);
INSERT INTO `role_permission` VALUES (10, '2', 1);
INSERT INTO `role_permission` VALUES (11, '2', 8);
INSERT INTO `role_permission` VALUES (12, '2', 5);
INSERT INTO `role_permission` VALUES (13, '2', 6);
INSERT INTO `role_permission` VALUES (14, '3', 1);
INSERT INTO `role_permission` VALUES (15, '3', 5);
INSERT INTO `role_permission` VALUES (16, '3', 6);
INSERT INTO `role_permission` VALUES (17, '1', 10);

-- ----------------------------
-- Table structure for sale
-- ----------------------------
DROP TABLE IF EXISTS `sale`;
CREATE TABLE `sale`  (
  `sale_id` bigint NOT NULL AUTO_INCREMENT COMMENT '药店id',
  `sale_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '药店名称',
  `sale_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `createtime` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`sale_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '药店信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sale
-- ----------------------------
INSERT INTO `sale` VALUES (1, '老百姓大药房（朝阳店）', '010-88880001', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `sale` VALUES (2, '益丰大药房（浦东店）', '021-88880002', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `sale` VALUES (3, '大参林药店（广州店）', '020-88880003', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `sale` VALUES (4, '一心堂药店（杭州店）', '0571-88880004', '2026-07-28 10:46:51', '2026-07-28 10:46:51');
INSERT INTO `sale` VALUES (5, '国大药房（南京店）', '025-88880005', '2026-07-28 10:46:51', '2026-07-28 10:46:51');

-- ----------------------------
-- Table structure for sysregion
-- ----------------------------
DROP TABLE IF EXISTS `sysregion`;
CREATE TABLE `sysregion`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '区域主键',
  `CityCode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市编码',
  `MerName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组合名称',
  `Lat` float NULL DEFAULT NULL COMMENT '纬度',
  `Lng` float NULL DEFAULT NULL COMMENT '经度',
  `Level` int NULL DEFAULT NULL COMMENT '区域等级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '城市信息详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sysregion
-- ----------------------------
INSERT INTO `sysregion` VALUES (11, 'BJ110105', '北京市朝阳区', 39.9219, 116.443, 3);
INSERT INTO `sysregion` VALUES (12, 'BJ110108', '北京市海淀区', 39.9561, 116.311, 3);
INSERT INTO `sysregion` VALUES (13, 'BJ110101', '北京市东城区', 39.9289, 116.416, 3);
INSERT INTO `sysregion` VALUES (14, 'BJ110102', '北京市西城区', 39.9123, 116.366, 3);
INSERT INTO `sysregion` VALUES (21, 'SH310115', '上海市浦东新区', 31.2213, 121.544, 3);
INSERT INTO `sysregion` VALUES (22, 'SH310101', '上海市黄浦区', 31.2316, 121.47, 3);
INSERT INTO `sysregion` VALUES (31, 'GD440100', '广东省广州市', 23.1292, 113.264, 2);
INSERT INTO `sysregion` VALUES (32, 'GD440300', '广东省深圳市', 22.5431, 114.058, 2);
INSERT INTO `sysregion` VALUES (41, 'ZJ330100', '浙江省杭州市', 30.2741, 120.155, 2);
INSERT INTO `sysregion` VALUES (51, 'JS320100', '江苏省南京市', 32.0584, 118.797, 2);
INSERT INTO `sysregion` VALUES (61, 'HB420100', '湖北省武汉市', 30.5928, 114.305, 2);
INSERT INTO `sysregion` VALUES (71, 'SC510100', '四川省成都市', 30.5702, 104.065, 2);

-- ----------------------------
-- Table structure for treat_type
-- ----------------------------
DROP TABLE IF EXISTS `treat_type`;
CREATE TABLE `treat_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '诊治类型id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '诊治类型名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '诊治类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of treat_type
-- ----------------------------
INSERT INTO `treat_type` VALUES (1, '门诊');
INSERT INTO `treat_type` VALUES (2, '急诊');
INSERT INTO `treat_type` VALUES (3, '住院');
INSERT INTO `treat_type` VALUES (4, '日间手术');
INSERT INTO `treat_type` VALUES (5, '家庭医生签约服务');
INSERT INTO `treat_type` VALUES (6, '互联网诊疗');

-- ----------------------------
-- Table structure for security_log
-- ----------------------------
DROP TABLE IF EXISTS `security_log`;
CREATE TABLE `security_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人用户名',
  `user_role` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人角色（1管理员，2医生，3患者）',
  `operation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作类型',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作详情',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URI',
  `http_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'HTTP方法',
  `status` int NULL DEFAULT NULL COMMENT '操作状态（1成功，0失败）',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作结果描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_operation`(`operation`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '安全日志表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
