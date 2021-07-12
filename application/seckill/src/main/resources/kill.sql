/*
 Navicat Premium Data Transfer

 Source Server         : name
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 121.196.152.52:3306
 Source Schema         : kill

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 10/01/2021 11:53:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品的图片',
  `goods_detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品的详情介绍',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `goods_stock` int(0) NULL DEFAULT 0 COMMENT '商品库存，-1表示没有限制',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES (1, 'iphoneX', 'Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机', '/img/iphonex.png', 'Apple iPhone X (A1865) 64GB 银色 移动联通电信4G手机', 8765.00, 10000);
INSERT INTO `goods` VALUES (2, '华为Meta9', '华为 Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '/img/meta10.png', '华为 Mate 9 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', 3212.00, -1);
INSERT INTO `goods` VALUES (3, 'iphone8', 'Apple iPhone 8 (A1865) 64GB 银色 移动联通电信4G手机', '/img/iphone8.png', 'Apple iPhone 8 (A1865) 64GB 银色 移动联通电信4G手机', 5589.00, 10000);
INSERT INTO `goods` VALUES (4, '小米6', '小米6 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', '/img/mi6.png', '小米6 4GB+32GB版 月光银 移动联通电信4G手机 双卡双待', 3212.00, 10000);

-- ----------------------------
-- Table structure for miaosha_goods
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品表',
  `goods_id` bigint(0) NULL DEFAULT NULL COMMENT '商品Id',
  `miaosha_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '秒杀价',
  `stock_count` int(0) NULL DEFAULT NULL COMMENT '库存数量',
  `start_date` datetime(0) NULL DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of miaosha_goods
-- ----------------------------
INSERT INTO `miaosha_goods` VALUES (1, 1, 0.01, 7, '2017-12-04 21:51:23', '2021-01-02 21:51:27');
INSERT INTO `miaosha_goods` VALUES (2, 2, 0.01, 9, '2017-12-04 21:40:14', '2021-01-01 14:00:24');
INSERT INTO `miaosha_goods` VALUES (3, 3, 0.01, 9, '2017-12-04 21:40:14', '2020-12-31 14:00:24');
INSERT INTO `miaosha_goods` VALUES (4, 4, 0.01, 7, '2017-12-04 21:40:14', '2021-01-10 14:00:24');

-- ----------------------------
-- Table structure for miaosha_order
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_order`;
CREATE TABLE `miaosha_order`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `order_id` bigint(0) NULL DEFAULT NULL COMMENT '订单ID',
  `goods_id` bigint(0) NULL DEFAULT NULL COMMENT '商品ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `u_uid_gid`(`user_id`, `goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of miaosha_order
-- ----------------------------

-- ----------------------------
-- Table structure for miaosha_user
-- ----------------------------
DROP TABLE IF EXISTS `miaosha_user`;
CREATE TABLE `miaosha_user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `register_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of miaosha_user
-- ----------------------------
INSERT INTO `miaosha_user` VALUES (1, 'username', 'password', 'salt', '2021-01-05 14:47:37');
INSERT INTO `miaosha_user` VALUES (2, 'username', 'password', 'salt', '2021-01-05 14:53:22');
INSERT INTO `miaosha_user` VALUES (3, 'username', 'password', 'salt', '2021-01-05 14:54:06');
INSERT INTO `miaosha_user` VALUES (4, 'username', 'password', 'salt', '2021-01-05 14:54:16');
INSERT INTO `miaosha_user` VALUES (5, '15136969122', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 20:45:16');
INSERT INTO `miaosha_user` VALUES (6, '15136969152', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 20:48:11');
INSERT INTO `miaosha_user` VALUES (7, 'tom', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 20:49:43');
INSERT INTO `miaosha_user` VALUES (8, '15136969155', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 20:50:06');
INSERT INTO `miaosha_user` VALUES (9, '15136969150', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 21:28:54');
INSERT INTO `miaosha_user` VALUES (10, '15136969145', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 21:29:06');
INSERT INTO `miaosha_user` VALUES (11, '123', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-06 21:30:59');
INSERT INTO `miaosha_user` VALUES (12, '18912341238', 'b7797cce01b4b131b433b6acf4add449', '1a2b3c4d', '2021-01-07 08:51:18');
INSERT INTO `miaosha_user` VALUES (13, '132', 'e53d30b06d4d0fb2e0d0bd6412e66440', '1a2b3c4d', '2021-01-07 14:00:51');
INSERT INTO `miaosha_user` VALUES (14, '1321', 'e53d30b06d4d0fb2e0d0bd6412e66440', '1a2b3c4d', '2021-01-07 14:22:37');

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `goods_id` bigint(0) NULL DEFAULT NULL COMMENT '商品ID',
  `delivery_addr_id` bigint(0) NULL DEFAULT NULL COMMENT '收获地址ID',
  `goods_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` int(0) NULL DEFAULT 0 COMMENT '商品数量',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品单价',
  `order_channel` tinyint(0) NULL DEFAULT 0 COMMENT '1pc，2android，3ios',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '订单的创建时间',
  `pay_date` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of order_info
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `register_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Joshua', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
