/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50635
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50635
File Encoding         : 65001

Date: 2020-05-08 15:46:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for formtest
-- ----------------------------
DROP TABLE IF EXISTS `formtest`;
CREATE TABLE `formtest` (
  `formId` varchar(20) DEFAULT NULL,
  `formType` varchar(10) DEFAULT NULL,
  `form` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
