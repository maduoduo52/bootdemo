DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                           `id` bigint(20) NOT NULL COMMENT '菜单ID',
                           `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
                           `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型  MENU 菜单  BUTTON 按钮（按钮包括权限）',
                           `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态  1 启用 0禁用',
                           `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
                           `parent` bigint(20) DEFAULT NULL COMMENT '父节点',
                           `parents` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '父节点集合',
                           `root_flag` tinyint(1) DEFAULT 0 COMMENT '是否为根节点',
                           `order_key` int(11) DEFAULT NULL COMMENT '排序',
                           `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url 访问地址  主要为前端地址',
                           `authority_url` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '权限地址  后台java接口访问地址',
                           `authority_button` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '权限按钮  此按钮IDS来控制 界面按钮或者字段显示与否',
                           `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                           `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                           `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                           `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                           `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                           `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '菜单管理', 'MENU', 1, 'fa fa-user', NULL, NULL, 1, NULL, '6', '/index.html,/logOut.html', '3', NULL, '2018-10-29 15:42:23', 0, 0, NULL, 11);
INSERT INTO `sys_menu` VALUES (506467201028325376, '系统管理', 'MENU', 1, 'fa fa-gears', 1, NULL, 0, NULL, NULL, NULL, NULL, '2018-10-29 13:59:50', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_menu` VALUES (506467317385314304, '组织机构管理', 'MENU', 1, 'fa fa-users', 506467201028325376, NULL, 0, NULL, './org/list.html', '/org/list.html,/org/getAll.html,/org/savePage.html,/org/save.html,/org/delete.html', NULL, '2018-10-29 14:00:18', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_menu` VALUES (506467471785467904, '用户管理', 'MENU', 1, 'fa fa-user', 506467201028325376, NULL, 0, NULL, './user/list.html', '/user/list.html,/user/selectUserList.html,/user/savePage.html,/user/delete.html,/user/authority.html,/user/save.html,/menu/getAll.html,/user/saveAuthority.html', NULL, '2018-10-29 14:00:55', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_menu` VALUES (506467554859425792, '职位管理', 'MENU', 1, 'fa fa-coffee', 506467201028325376, NULL, 0, NULL, './post/list.html', '/post/list.html,/post/selectList.html,/post/savePage.html,/post/delete.html,/post/authority.html,/post/save.html,/menu/getAll.html,/post/saveAuthority.html,/post/saveUserPage.html,/post/saveUser.html', NULL, '2018-10-29 14:01:15', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_menu` VALUES (506467642837979136, '菜单管理', 'MENU', 1, 'fa fa-navicon', 506467201028325376, NULL, 0, NULL, './menu/list.html', '/menu/list.html,/menu/savePage.html,/menu/save.html,/menu/delete.html', NULL, '2018-10-29 14:01:36', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_menu` VALUES (506493053107036160, '1', 'MENU', 1, '1', 506467201028325376, NULL, 0, NULL, '1', '1', '1', '2018-10-29 15:42:34', '2018-10-29 15:42:26', 0, 1, 11, 11);

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org`  (
                          `id` bigint(20) NOT NULL,
                          `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
                          `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'company 公司 department 部门',
                          `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态  1 启用 0禁用',
                          `parent` bigint(20) DEFAULT NULL COMMENT '父节点',
                          `parents` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '父节点集合',
                          `root_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为根节点',
                          `province` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '身份编码',
                          `city` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市 编码 ',
                          `area` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区编码',
                          `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
                          `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                          `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                          `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                          `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                          `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                          `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_org
-- ----------------------------
INSERT INTO `sys_org` VALUES (1, '多米科技', 'company', 1, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_org` VALUES (1055351814632153090, '66664444444', 'department', 0, 1055351814632153090, NULL, 0, NULL, NULL, NULL, NULL, '2018-10-25 14:54:15', '2018-10-25 14:56:10', 0, 1, NULL, NULL);
INSERT INTO `sys_org` VALUES (1055351949579689986, '4444', 'company', 1, 1055351814632153090, NULL, 0, NULL, NULL, NULL, NULL, '2018-10-25 14:54:47', '2018-10-25 14:54:39', 0, 1, NULL, NULL);
INSERT INTO `sys_org` VALUES (1055352377239293953, '9999', 'company', 1, 1, NULL, 0, NULL, NULL, NULL, NULL, '2018-10-25 14:56:29', NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_org` VALUES (1056809114832076801, '12412433', 'company', 1, 1055352377239293953, NULL, 0, NULL, NULL, NULL, NULL, '2018-10-29 15:25:02', '2018-10-29 15:26:26', 0, 1, 11, 11);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
                           `id` bigint(20) NOT NULL,
                           `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
                           `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态  1 启用 0禁用',
                           `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机构ID',
                           `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                           `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                           `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                           `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                           `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                           `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, '测试', 1, '1', '2018-10-25 17:21:53', '2018-10-29 15:40:35', 0, 0, NULL, 11);
INSERT INTO `sys_post` VALUES (2, '测试23', 1, '1', '2018-10-25 17:21:50', '2018-10-25 17:25:40', 0, 1, NULL, NULL);
INSERT INTO `sys_post` VALUES (3, '测试78', 1, '1', '2018-10-25 17:21:55', '2018-10-29 15:40:26', 0, 1, NULL, 11);
INSERT INTO `sys_post` VALUES (1055390337020071937, '124124', 1, '1', '2018-10-25 17:27:19', '2018-10-25 17:27:09', 0, 1, NULL, NULL);

-- ----------------------------
-- Table structure for sys_post_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_post_menu`;
CREATE TABLE `sys_post_menu`  (
                                `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `post_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                                `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                                `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                                `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                                `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                                `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职位 - 菜单 关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post_menu
-- ----------------------------
INSERT INTO `sys_post_menu` VALUES ('506479747917406208', '1', '3', '2018-10-29 14:49:42', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506479747953606656', '506467201028325376', '3', '2018-10-29 14:49:42', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506479747973300224', '506467317385314304', '3', '2018-10-29 14:49:42', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506479747995443200', '506467642837979136', '3', '2018-10-29 14:49:42', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506492611124342784', '1', '1', '2018-10-29 15:40:49', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506492611188887552', '506467201028325376', '1', '2018-10-29 15:40:49', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506492611201720320', '506467317385314304', '1', '2018-10-29 15:40:49', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506492611215228928', '506467471785467904', '1', '2018-10-29 15:40:49', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506492611232235520', '506467554859425792', '1', '2018-10-29 15:40:49', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_post_menu` VALUES ('506492611245940736', '506467642837979136', '1', '2018-10-29 15:40:49', NULL, 0, 0, 11, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                           `id` bigint(20) NOT NULL COMMENT 'ID',
                           `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
                           `login_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
                           `salt` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盐',
                           `pwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
                           `status` int(11) NOT NULL DEFAULT 1 COMMENT '0 禁用  1启用',
                           `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机构ID',
                           `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
                           `id_card` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '身份证号码',
                           `sex` int(11) DEFAULT 1 COMMENT '性别 1: 男  2：女  3：其他',
                           `admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为超级管理员',
                           `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                           `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                           `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                           `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                           `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                           `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (11, '马多多', 'test', '486524236495458304', '7907804707b46ab25dd27168f272c7b9', 1, NULL, '18117906858', NULL, 1, 1, NULL, '2018-12-01 21:52:44', NULL, 0, NULL, 11);
INSERT INTO `sys_user` VALUES (1056785448924749825, '11', '33', '506464975222059008', '44', 1, '1', '22', NULL, 1, 0, '2018-10-29 13:51:00', NULL, 0, 0, 11, NULL);

-- ----------------------------
-- Table structure for sys_user_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_menu`;
CREATE TABLE `sys_user_menu`  (
                                `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
                                `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单ID',
                                `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                                `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                                `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                                `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                                `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                                `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户 - 菜单 关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_menu
-- ----------------------------
INSERT INTO `sys_user_menu` VALUES ('506478541129338880', '1056785448924749825', '1', '2018-10-29 14:44:54', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506478541158694912', '1056785448924749825', '506467201028325376', '2018-10-29 14:44:54', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506478541177024512', '1056785448924749825', '506467317385314304', '2018-10-29 14:44:54', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506478541193805824', '1056785448924749825', '506467471785467904', '2018-10-29 14:44:54', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506478541200023552', '1056785448924749825', '506467554859425792', '2018-10-29 14:44:54', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506478541214613504', '1056785448924749825', '506467642837979136', '2018-10-29 14:44:54', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506487418554478592', '11', '1', '2018-10-29 15:20:11', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506487418604519424', '11', '506467201028325376', '2018-10-29 15:20:11', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506487418635804672', '11', '506467317385314304', '2018-10-29 15:20:11', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506487418651660288', '11', '506467471785467904', '2018-10-29 15:20:11', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506487418669830144', '11', '506467554859425792', '2018-10-29 15:20:11', NULL, 0, 0, 11, NULL);
INSERT INTO `sys_user_menu` VALUES ('506487418698891264', '11', '506467642837979136', '2018-10-29 15:20:11', NULL, 0, 0, 11, NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
                                `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
                                `post_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职位ID',
                                `add_time` datetime(0) DEFAULT NULL COMMENT '添加时间',
                                `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
                                `version` bigint(20) DEFAULT NULL COMMENT '版本号',
                                `delete_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除0:未删除 1:已删除',
                                `add_emp_id` bigint(20) DEFAULT NULL COMMENT '添加人',
                                `update_emp_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户 - 职位 关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES ('506414791125098496', '11', '3', '2018-10-29 10:31:35', NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_user_post` VALUES ('506414791140552704', '1055380556909793282', '3', '2018-10-29 10:31:35', NULL, 0, 0, NULL, NULL);
INSERT INTO `sys_user_post` VALUES ('506492787681296384', '11', '1', '2018-10-29 15:41:31', NULL, 0, 0, 11, NULL);

SET FOREIGN_KEY_CHECKS = 1;