/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.109(thyc)
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 192.168.1.109:3306
 Source Schema         : tld_admin

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 16/06/2020 12:00:40
*/


Create Database If Not Exists tld_admin Character Set UTF8 COLLATE utf8_bin;
USE tld_admin;
-- ----------------------------
-- Table structure for t_backstage_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_backstage_menu`;
CREATE TABLE `t_backstage_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '导航栏显示名',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '路由图标',
  `path` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '路径',
  `permission` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '权限',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '描述',
  `type` int(2) NULL DEFAULT NULL COMMENT '0-按钮 1-下拉菜单',
  `parent_id` int(4) NULL DEFAULT NULL COMMENT '父级id',
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '后台访问的url',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效（1-有效，0-无效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '后台菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_backstage_menu
-- ----------------------------
INSERT INTO `t_backstage_menu` VALUES (1, '添加用户', NULL, NULL, 'user:info:add', '新增后台用户', 0, 6, '', 'admin', NULL, '2019-08-31 11:47:07', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (2, '修改用户', NULL, NULL, 'user:info:update', '修改后台用户信息', 0, 6, '', 'admin', NULL, '2019-08-31 11:50:05', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (3, '删除用户', NULL, NULL, 'user:info:delete', '删除后台用户', 0, 6, '', 'admin', NULL, '2019-08-31 11:52:04', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (4, '添加菜单', NULL, NULL, 'menu:add', '添加菜单', 0, 6, '', 'admin', NULL, '2019-08-31 16:41:37', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (5, '菜单管理', NULL, NULL, 'menu:list', '菜单列表', 1, 6, NULL, 'admin', NULL, '2019-09-02 01:49:22', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (6, '后台管理', NULL, '前端路由', NULL, '后台管理顶级菜单', 0, 0, '', 'admin', NULL, '2020-04-21 15:23:59', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (15, '设置角色菜单', NULL, NULL, 'role:menu:add', '为角色设置访问菜单', 0, 6, NULL, 'admin', NULL, '2019-09-02 14:38:45', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (17, '获取角色菜单', NULL, NULL, 'role:menu:get', '查询角色菜单', 0, 6, NULL, 'admin', NULL, '2019-09-02 06:45:35', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (18, '设置用户角色', NULL, NULL, 'user:role:add', '为用户设置角色', 0, 6, NULL, 'admin', NULL, '2019-09-02 09:27:52', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (19, '新增角色', NULL, NULL, 'role:add', '添加新角色', 0, 6, NULL, 'admin', NULL, '2019-09-02 09:31:15', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (22, '用户管理', NULL, NULL, 'user:list', '查询用户列表', 1, 6, NULL, 'admin', NULL, '2019-09-03 10:55:27', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (23, '角色管理', NULL, NULL, 'role:list', '查询角色列表', 1, 6, NULL, 'admin', NULL, '2019-09-03 10:58:57', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (24, '删除角色', NULL, NULL, 'role:delete', '删除角色', 0, 6, NULL, 'admin', NULL, '2019-09-03 11:00:48', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (25, '修改角色', NULL, NULL, 'role:update', '修改角色信息', 0, 6, NULL, 'admin', NULL, '2019-09-03 11:01:26', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (26, '删除菜单', NULL, NULL, 'menu:delete', '删除菜单', 0, 6, NULL, 'admin', NULL, '2019-09-03 11:02:38', NULL, 1);
INSERT INTO `t_backstage_menu` VALUES (27, '修改菜单', NULL, NULL, 'menu:update', '修改菜单', 0, 6, NULL, 'admin', NULL, '2019-09-03 11:03:20', NULL, 1);

-- ----------------------------
-- Table structure for t_backstage_role
-- ----------------------------
DROP TABLE IF EXISTS `t_backstage_role`;
CREATE TABLE `t_backstage_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '角色说明',
  `status_id` int(11) NULL DEFAULT NULL COMMENT '状态ID',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效（1-有效，0-无效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '后台角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_backstage_role
-- ----------------------------
INSERT INTO `t_backstage_role` VALUES (1, 'admin', '管理员', NULL, NULL, 'admin', '2019-08-16 16:44:59', '2019-09-18 12:29:13', 1);
INSERT INTO `t_backstage_role` VALUES (2, 'user', '普通用户', NULL, NULL, NULL, '2019-08-16 16:45:08', NULL, 1);
INSERT INTO `t_backstage_role` VALUES (3, 'serverUser', 'fabric服务购买者', NULL, 'admin', NULL, '2019-09-07 06:50:49', NULL, 1);
INSERT INTO `t_backstage_role` VALUES (6, 'developer', '开发人员', NULL, 'admin', NULL, '2019-09-18 13:39:20', NULL, 1);

-- ----------------------------
-- Table structure for t_backstage_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_backstage_role_menu`;
CREATE TABLE `t_backstage_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `back_role_id` int(11) NULL DEFAULT NULL COMMENT '后台角色ID',
  `back_menu_id` int(11) NULL DEFAULT NULL COMMENT '后台菜单ID',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效（1-有效，0-无效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 457 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '后台角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_backstage_role_menu
-- ----------------------------
INSERT INTO `t_backstage_role_menu` VALUES (1, 1, 1, 'admin', NULL, '2019-08-16 16:46:27', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (2, 1, 2, 'admin', NULL, '2019-08-16 16:46:29', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (26, 1, 15, 'admin', NULL, '2019-09-02 16:55:05', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (27, 1, 16, 'super', NULL, '2019-09-02 16:55:18', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (28, 1, 17, 'super', NULL, '2019-09-02 16:55:18', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (32, 1, 3, 'admin', NULL, '2019-09-02 17:26:41', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (33, 1, 4, 'admin', NULL, '2019-09-02 17:26:41', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (34, 1, 5, 'admin', NULL, '2019-09-02 17:26:41', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (35, 1, 18, 'admin', NULL, '2019-09-02 17:28:16', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (171, 1, 19, 'admin', NULL, '2019-09-02 17:58:38', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (172, 1, 22, 'admin', 'admin', '2019-09-03 10:56:27', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (173, 1, 23, 'admin', NULL, '2019-09-03 10:59:37', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (174, 1, 24, 'admin', NULL, '2019-09-03 11:03:57', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (175, 1, 25, 'admin', NULL, '2019-09-03 11:03:58', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (176, 1, 26, 'admin', NULL, '2019-09-03 11:03:59', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (177, 1, 27, 'admin', NULL, '2019-09-03 11:04:01', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (178, 1, 20, 'admin', NULL, '2019-09-03 11:04:58', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (179, 1, 21, 'admin', NULL, '2019-09-03 11:05:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (180, 1, 28, 'admin', NULL, '2019-09-05 18:18:39', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (181, 1, 29, 'admin', NULL, '2019-09-06 14:11:06', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (184, 1, 30, 'admin', NULL, '2019-09-07 15:32:38', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (185, 1, 31, 'admin', NULL, '2019-09-09 11:32:31', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (187, 1, 32, 'admin', NULL, '2019-09-09 11:37:50', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (190, 1, 33, 'admin', NULL, '2019-09-10 09:56:14', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (191, 1, 34, 'admin', NULL, '2019-09-10 14:30:13', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (196, 1, 38, 'admin', NULL, '2019-09-18 10:25:22', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (197, 1, 39, 'admin', NULL, '2019-09-18 10:25:25', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (198, 1, 40, 'admin', NULL, '2019-09-18 10:25:27', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (199, 1, 41, 'admin', NULL, '2019-09-18 10:25:30', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (200, 1, 42, 'admin', NULL, '2019-09-18 10:25:32', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (201, 1, 43, 'admin', NULL, '2019-09-18 10:25:34', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (202, 1, 44, 'admin', NULL, '2019-09-18 10:25:35', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (203, 1, 45, 'admin', NULL, '2019-09-18 10:25:36', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (205, 1, 54, 'super', NULL, '2019-09-18 21:06:14', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (207, 1, 52, 'admin', NULL, '2019-09-19 10:27:29', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (208, 1, 55, 'admin', NULL, '2019-09-20 10:35:53', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (209, 1, 35, 'super', NULL, '2019-09-23 14:56:03', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (210, 1, 56, 'super', NULL, '2019-09-23 14:56:03', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (211, 1, 57, 'super', NULL, '2019-09-23 20:13:53', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (212, 1, 58, 'super', NULL, '2019-09-25 15:12:13', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (213, 1, 59, 'super', NULL, '2019-09-25 16:17:11', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (214, 1, 60, 'super', NULL, '2019-09-25 16:42:50', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (215, 1, 61, 'super', NULL, '2019-09-25 17:33:38', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (216, 1, 62, 'super', NULL, '2019-09-26 11:13:24', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (217, 1, 36, 'super', NULL, '2019-09-27 14:34:29', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (218, 1, 63, 'super', NULL, '2019-09-27 14:34:29', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (219, 1, 64, 'super', NULL, '2019-09-27 14:39:21', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (220, 1, 65, 'super', NULL, '2019-10-12 17:39:52', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (221, 1, 66, 'super', NULL, '2019-10-17 13:55:36', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (222, 1, 37, 'super', NULL, '2019-10-18 17:37:46', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (223, 1, 67, 'super', NULL, '2019-10-18 17:37:46', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (224, 1, 68, 'super', NULL, '2019-10-22 11:02:36', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (225, 1, 69, 'super', NULL, '2019-10-23 15:43:23', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (288, 8, 5, 'admin', NULL, '2019-11-25 17:08:19', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (289, 8, 4, 'admin', NULL, '2019-11-25 17:08:19', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (290, 8, 16, 'admin', NULL, '2019-11-25 17:08:19', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (291, 8, 26, 'admin', NULL, '2019-11-25 17:08:19', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (292, 8, 27, 'admin', NULL, '2019-11-25 17:08:19', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (293, 8, 39, 'admin', NULL, '2019-11-25 17:08:19', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (418, 3, 30, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (419, 3, 31, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (420, 3, 40, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (421, 3, 28, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (422, 3, 29, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (423, 3, 57, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (424, 3, 58, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (425, 3, 59, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (426, 3, 66, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (427, 3, 71, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (428, 3, 41, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (429, 3, 34, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (430, 3, 37, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (431, 3, 67, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (432, 3, 42, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (433, 3, 36, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (434, 3, 63, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (435, 3, 43, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (436, 3, 64, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (437, 3, 44, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (438, 3, 35, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (439, 3, 56, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (440, 3, 65, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (441, 3, 45, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (442, 3, 33, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (443, 3, 62, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (444, 3, 69, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (445, 3, 55, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (446, 3, 60, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (447, 3, 61, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (448, 3, 68, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (449, 3, 38, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (450, 3, 39, 'admin', NULL, '2019-11-26 09:57:00', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (451, 2, 30, 'admin', NULL, '2019-11-27 15:28:09', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (452, 2, 31, 'admin', NULL, '2019-11-27 15:28:09', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (453, 2, 73, 'admin', NULL, '2019-11-27 15:28:09', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (454, 2, 68, 'admin', NULL, '2019-11-27 15:28:09', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (455, 2, 38, 'admin', NULL, '2019-11-27 15:28:09', NULL, 1);
INSERT INTO `t_backstage_role_menu` VALUES (456, 2, 39, 'admin', NULL, '2019-11-27 15:28:09', NULL, 1);

-- ----------------------------
-- Table structure for t_backstage_user
-- ----------------------------
DROP TABLE IF EXISTS `t_backstage_user`;
CREATE TABLE `t_backstage_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号码',
  `login_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后一次登录ip',
  `login_address` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后一次登录地址(国家/省/城市)',
  `operation_status` int(10) NULL DEFAULT NULL COMMENT '操作状态',
  `status_id` int(11) NULL DEFAULT 1 COMMENT '状态ID',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效（1-有效，0-无效）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_c`(`user_name`, `email`, `telephone`) USING BTREE COMMENT '用户名/邮箱/手机号码唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '后台用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_backstage_user
-- ----------------------------
INSERT INTO `t_backstage_user` VALUES (1, 'admin', '51AC53D7D85D431782023168B5DE1E555545F90CEC1A83A80E199075', '1299986041@qq.com', '18208201778', '192.168.56.1', '中国|华南|湖南省|长沙市|电信', 4, 1, 'esbug', 'admin', '2019-08-16 16:44:47', '2019-10-08 08:15:14', 1);
INSERT INTO `t_backstage_user` VALUES (11, 'esbug', '51AC53D7D85D431782023168B5DE1E555545F90CEC1A83A80E199075', '1468946038@qq.com', '18692345391', '192.168.56.1', '中国|华南|湖南省|长沙市|电信', NULL, 1, 'admin', NULL, '2019-10-28 17:43:13', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (12, 'xiaoming123', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, '', NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 16:20:40', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (13, 'xiaoming124', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', '', '', '', '', NULL, 1, '', '', '2020-04-20 16:20:40', '2020-04-20 16:20:40', 1);
INSERT INTO `t_backstage_user` VALUES (14, 'xiaoming1241', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:09', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (15, 'xiaoming1242', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:11', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (16, 'xiaoming1243', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:13', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (17, 'xiaoming1244', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:15', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (18, 'xiaoming1245', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:17', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (19, 'xiaoming1246', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:22', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (20, 'xiaoming1247', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:25', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (21, 'xiaoming1248', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:27', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (22, 'xiaoming1249', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:29', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (23, 'xiaoming12410', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:43', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (24, 'xiaoming12411', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:46', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (25, 'xiaoming12412', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:47', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (26, 'xiaoming12413', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:48', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (27, 'xiaoming12414', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:49', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (28, 'xiaoming12415', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:52', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (29, 'xiaoming12416', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:54', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (30, 'xiaoming12417', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:56', NULL, 1);
INSERT INTO `t_backstage_user` VALUES (31, 'xiaoming12418', 'A58181837D08B77F36FA298260E4FCE6627B4BD75487DB8E8D850B9D', NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, '2020-04-20 18:30:58', NULL, 1);

-- ----------------------------
-- Table structure for t_backstage_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_backstage_user_role`;
CREATE TABLE `t_backstage_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `back_user_id` int(11) NULL DEFAULT NULL COMMENT '后台用户ID',
  `back_role_id` int(11) NULL DEFAULT NULL COMMENT '后台角色ID',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效（1-有效，0-无效）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '后台用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_backstage_user_role
-- ----------------------------
INSERT INTO `t_backstage_user_role` VALUES (22, 1, 1, NULL, NULL, '2019-09-19 11:02:26', NULL, 1);
INSERT INTO `t_backstage_user_role` VALUES (23, 1, 2, NULL, NULL, '2019-09-19 11:02:26', NULL, 1);
INSERT INTO `t_backstage_user_role` VALUES (24, 1, 3, NULL, NULL, '2019-09-19 11:02:26', NULL, 1);
INSERT INTO `t_backstage_user_role` VALUES (25, 1, 8, NULL, NULL, '2019-09-19 11:02:26', NULL, 1);
INSERT INTO `t_backstage_user_role` VALUES (75, 11, 2, NULL, NULL, '2019-12-09 16:40:40', NULL, 1);
INSERT INTO `t_backstage_user_role` VALUES (76, 11, 3, NULL, NULL, '2019-12-09 16:40:40', NULL, 1);
INSERT INTO `t_backstage_user_role` VALUES (77, 11, 8, NULL, NULL, '2019-12-09 16:40:40', NULL, 1);

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '字典编码',
  `dict_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '字典名',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父ID',
  `level` int(4) NULL DEFAULT NULL COMMENT '级别',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '描述',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者ID',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效( 1-有效, 0-无效 )',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_file_cache
-- ----------------------------
DROP TABLE IF EXISTS `t_file_cache`;
CREATE TABLE `t_file_cache`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件名',
  `title_suffix` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件名后缀',
  `file_size` bigint(20) NULL DEFAULT NULL COMMENT '文件大小',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '链接',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者ID',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效( 1-有效, 0-无效 )',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '文件存储' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_operation_logger
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_logger`;
CREATE TABLE `t_operation_logger`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operation_content` varchar(150) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作内容',
  `operation_type` int(1) NULL DEFAULT NULL COMMENT '操作类型( 0-未设置, 1-新增, 2-删除, 3-修改, 4-查询)',
  `log_class_name` varchar(150) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类名',
  `log_method_name` varchar(150) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '方法名',
  `params_type_list` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '参数类型列表',
  `params_name_list` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '参数名称列表',
  `params_value_list` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '参数值列表',
  `exec_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '执行时间(毫秒)',
  `exception_info` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '异常信息',
  `remote_ip_address` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '远程IP地址',
  `access_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '访问类型(0-web, 1-android, 2-ios)',
  `city_info` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '城市信息',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者ID',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效( 1-有效, 0-无效 )',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `t_schedule_job`;
CREATE TABLE `t_schedule_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '任务名',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '任务分组',
  `cron` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Cron表达式',
  `concurrent` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '执行状态()',
  `bean_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '执行类',
  `method_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '执行方法',
  `params` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '初始化参数',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者ID',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效( 1-有效, 0-无效 )',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '定时任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_status
-- ----------------------------
DROP TABLE IF EXISTS `t_status`;
CREATE TABLE `t_status`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `status_code` int(6) NULL DEFAULT NULL COMMENT '状态编码',
  `status_en_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态英文描述',
  `status_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '描述',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建者ID',
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改者ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `valid` tinyint(1) NULL DEFAULT 1 COMMENT '是否有效( 1-有效, 0-无效 )',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '状态信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_status
-- ----------------------------
INSERT INTO `t_status` VALUES (1, 10000, 'NORMAL', '正常', 'normal status', 'admin', NULL, '2019-08-26 16:05:07', NULL, 1);
INSERT INTO `t_status` VALUES (2, 10001, 'INVALID', '无效', NULL, 'admin', NULL, '2019-08-26 16:05:37', NULL, 1);
INSERT INTO `t_status` VALUES (3, 10002, 'LOCK', '已锁定', NULL, 'admin', NULL, '2019-08-26 16:08:21', NULL, 1);
INSERT INTO `t_status` VALUES (4, 10003, 'DISABLED', '未激活', NULL, 'admin', NULL, '2019-09-06 16:45:07', NULL, 1);
INSERT INTO `t_status` VALUES (5, 10004, 'NOCONFIG', '未配置', NULL, 'admin', NULL, '2019-09-20 20:04:57', NULL, 1);
INSERT INTO `t_status` VALUES (6, 10005, 'RUNNING', '运行中', NULL, 'admin', NULL, '2019-09-21 15:38:48', NULL, 1);
INSERT INTO `t_status` VALUES (7, 10006, 'STOPPED', '已停止', NULL, 'admin', NULL, '2019-09-21 15:38:52', NULL, 1);
INSERT INTO `t_status` VALUES (8, 10007, 'CONFIGURED', '已配置', NULL, 'admin', NULL, '2019-09-24 14:29:14', NULL, 1);
INSERT INTO `t_status` VALUES (9, 10008, 'ACTIVATED', '已激活', NULL, 'admin', NULL, '2019-09-25 14:54:47', NULL, 1);
INSERT INTO `t_status` VALUES (10, 10009, 'PENDING', '待审批', NULL, 'admin', NULL, '2019-09-25 16:10:03', NULL, 1);
INSERT INTO `t_status` VALUES (11, 10010, 'BOOTFAILED', '启动失败', NULL, 'admin', NULL, '2019-09-27 19:37:49', NULL, 1);
INSERT INTO `t_status` VALUES (12, 10011, 'NOINSTALL', '未安装', NULL, 'admin', NULL, '2019-09-28 09:23:02', NULL, 1);
INSERT INTO `t_status` VALUES (13, 10012, 'NOINSTANTIATION', '未实例化', NULL, 'admin', NULL, '2019-09-28 09:23:51', NULL, 1);
INSERT INTO `t_status` VALUES (14, 10013, 'CONFIGURING', '配置中', NULL, 'admin', NULL, '2019-09-28 10:20:16', NULL, 1);
INSERT INTO `t_status` VALUES (15, 10014, 'AGRRED', '已同意', NULL, 'admin', NULL, '2019-10-17 10:27:10', NULL, 1);
INSERT INTO `t_status` VALUES (16, 100015, 'BUILDING_CONTAINER', '正在构建容器', NULL, 'admin', NULL, '2019-10-21 14:02:04', NULL, 1);
INSERT INTO `t_status` VALUES (17, 100016, 'RUNNING_CONTAINER', '正在启动容器', NULL, 'admin', NULL, '2019-10-21 14:02:26', NULL, 1);
INSERT INTO `t_status` VALUES (18, 100017, 'ACTIVATING', '激活中', NULL, 'admin', NULL, '2019-10-22 15:31:58', NULL, 1);
INSERT INTO `t_status` VALUES (19, 100018, 'INSTALLING', '正在安装', NULL, 'admin', NULL, '2019-10-31 16:32:55', NULL, 1);
INSERT INTO `t_status` VALUES (20, 100019, 'INSTANTIATING', '正在实例化', NULL, 'admin', NULL, '2019-10-31 16:42:04', NULL, 1);
INSERT INTO `t_status` VALUES (21, 100020, 'UPGRADING', '正在升级', NULL, 'admin', NULL, '2019-11-14 11:26:23', NULL, 1);


SET FOREIGN_KEY_CHECKS = 1;
