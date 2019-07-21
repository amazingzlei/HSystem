/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50015
Source Host           : localhost:3306
Source Database       : hsystem

Target Server Type    : MYSQL
Target Server Version : 50015
File Encoding         : 65001

Date: 2019-07-21 18:39:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admedicine`
-- ----------------------------
DROP TABLE IF EXISTS `admedicine`;
CREATE TABLE `admedicine` (
  `aid` varchar(15) NOT NULL COMMENT '补药流水号',
  `createTime` varchar(30) default NULL COMMENT '创建时间',
  `wid` varchar(15) default NULL COMMENT '补药人',
  PRIMARY KEY  (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admedicine
-- ----------------------------

-- ----------------------------
-- Table structure for `admedicineinfo`
-- ----------------------------
DROP TABLE IF EXISTS `admedicineinfo`;
CREATE TABLE `admedicineinfo` (
  `aid` varchar(15) NOT NULL default '' COMMENT '补药流水id',
  `mid` varchar(15) NOT NULL COMMENT '药品id',
  `num` int(4) default NULL COMMENT '药品数量',
  `mProductTime` date NOT NULL default '0000-00-00' COMMENT '药品生产日期',
  PRIMARY KEY  (`aid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admedicineinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `counter`
-- ----------------------------
DROP TABLE IF EXISTS `counter`;
CREATE TABLE `counter` (
  `cid` int(15) NOT NULL auto_increment,
  `position` varchar(15) default NULL,
  `isUse` int(11) default NULL COMMENT '是否使用',
  PRIMARY KEY  (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of counter
-- ----------------------------
INSERT INTO `counter` VALUES ('1', '柜台10001', '1');
INSERT INTO `counter` VALUES ('2', '柜台10002', '1');
INSERT INTO `counter` VALUES ('3', '柜台10003', '1');
INSERT INTO `counter` VALUES ('4', '柜台10004', '1');
INSERT INTO `counter` VALUES ('5', '柜台10005', '1');
INSERT INTO `counter` VALUES ('6', '柜台10006', '1');
INSERT INTO `counter` VALUES ('7', '柜台10007', '1');
INSERT INTO `counter` VALUES ('8', '柜台10008', '1');
INSERT INTO `counter` VALUES ('9', '柜台10009', '1');
INSERT INTO `counter` VALUES ('10', '柜台10010', '1');
INSERT INTO `counter` VALUES ('11', '柜台10011', '1');
INSERT INTO `counter` VALUES ('12', '柜台10012', '1');
INSERT INTO `counter` VALUES ('13', '柜台10013', '1');
INSERT INTO `counter` VALUES ('14', '柜台10014', '1');
INSERT INTO `counter` VALUES ('15', '柜台10015', '1');
INSERT INTO `counter` VALUES ('16', '柜台10016', '1');
INSERT INTO `counter` VALUES ('17', '柜台10017', '1');
INSERT INTO `counter` VALUES ('18', '柜台10018', '1');
INSERT INTO `counter` VALUES ('19', '柜台10019', '1');
INSERT INTO `counter` VALUES ('20', '柜台10020', '1');
INSERT INTO `counter` VALUES ('21', '柜台10021', '1');
INSERT INTO `counter` VALUES ('22', '柜台10022', '1');
INSERT INTO `counter` VALUES ('23', '柜台10023', '1');
INSERT INTO `counter` VALUES ('24', '柜台10024', '1');
INSERT INTO `counter` VALUES ('25', '柜台10025', '1');
INSERT INTO `counter` VALUES ('26', '柜台10026', '0');
INSERT INTO `counter` VALUES ('27', '柜台10027', '0');

-- ----------------------------
-- Table structure for `depart`
-- ----------------------------
DROP TABLE IF EXISTS `depart`;
CREATE TABLE `depart` (
  `id` int(5) NOT NULL auto_increment COMMENT '科室id',
  `name` varchar(15) default NULL COMMENT '科室名',
  `createTime` date default NULL COMMENT '创建时间',
  `updateTime` date default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of depart
-- ----------------------------
INSERT INTO `depart` VALUES ('1', '内科', '2019-02-21', '2019-02-21');
INSERT INTO `depart` VALUES ('3', '外科', '2019-02-21', '2019-02-21');
INSERT INTO `depart` VALUES ('4', '肠胃科', '2019-05-15', '2019-05-15');
INSERT INTO `depart` VALUES ('5', '骨科', '2019-05-15', '2019-05-15');
INSERT INTO `depart` VALUES ('6', '神经内科', '2019-05-15', '2019-05-15');
INSERT INTO `depart` VALUES ('7', '心血管内科', '2019-05-15', '2019-05-15');
INSERT INTO `depart` VALUES ('8', '泌尿外科', '2019-05-15', '2019-05-15');
INSERT INTO `depart` VALUES ('9', '神经外科', '2019-05-15', '2019-05-15');
INSERT INTO `depart` VALUES ('10', '儿科', '2019-05-17', '2019-05-17');

-- ----------------------------
-- Table structure for `employee`
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `wid` int(15) NOT NULL auto_increment COMMENT '工号',
  `name` varchar(15) default NULL COMMENT '姓名',
  `gender` varchar(2) default NULL COMMENT '性别',
  `age` varchar(2) default NULL COMMENT '年龄',
  `phone` varchar(13) default NULL COMMENT '电话',
  `type` varchar(4) default NULL COMMENT '类型',
  `password` varchar(15) default NULL COMMENT '密码',
  `departId` int(5) default NULL COMMENT '部门id',
  `create_time` varchar(30) default NULL COMMENT '创建时间',
  `update_time` varchar(30) default NULL COMMENT '更新时间',
  PRIMARY KEY  (`wid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('10001', '管理员', '男', '36', '13665217882', '0', '10001', null, '2019-02-21 00:00:00', '2019-03-30 13:40:07');
INSERT INTO `employee` VALUES ('10002', '赵医生', '女', '36', '13665217882', '1', '000000', '1', '2019-02-21 00:00:00', '2019-02-22 00:00:00');
INSERT INTO `employee` VALUES ('10003', '钱主管', '男', '52', '13665217882', '2', '000000', null, '2019-02-21 00:00:00', '2019-02-21 00:00:00');
INSERT INTO `employee` VALUES ('10004', '李采购', '男', '23', '13665217882', '3', '000000', null, '2019-02-21 00:00:00', '2019-02-21 00:00:00');
INSERT INTO `employee` VALUES ('10005', '王医生', '女', '45', '13665217882', '1', '000000', '3', '2019-02-21 00:00:00', '2019-02-21 00:00:00');
INSERT INTO `employee` VALUES ('10007', '黄出药', '男', '42', '13665217882', '4', '000000', null, '2019-02-22 00:00:00', '2019-02-22 00:00:00');
INSERT INTO `employee` VALUES ('10008', '曾主任', '男', '26', '13665217882', '5', '000000', '3', '2019-02-22 00:00:00', '2019-04-10 13:35:58');
INSERT INTO `employee` VALUES ('10009', '周收费', '女', '25', '13665217882', '6', '000000', null, '2019-03-28 00:00:00', '2019-03-28 00:00:00');
INSERT INTO `employee` VALUES ('10010', '朱医生', '男', '26', '13665217882', '1', '000000', '1', '2019-03-30 13:40:52', '2019-03-30 13:40:52');
INSERT INTO `employee` VALUES ('10011', '蔡主任', '男', '25', '13665217882', '5', '000000', '1', '2019-03-30 14:38:10', '2019-04-10 12:55:25');
INSERT INTO `employee` VALUES ('10012', '朱医生', '男', '26', '13665217882', '1', '000000', '8', '2019-05-15 14:13:58', '2019-05-15 14:13:58');
INSERT INTO `employee` VALUES ('10013', '周主任', '男', '30', '13665217882', '5', '000000', '4', '2019-05-15 14:24:08', '2019-05-15 14:24:08');
INSERT INTO `employee` VALUES ('10014', '吴医生', '男', '26', '13665217882', '1', '000000', '1', '2019-05-17 20:59:51', '2019-05-17 20:59:51');

-- ----------------------------
-- Table structure for `etype`
-- ----------------------------
DROP TABLE IF EXISTS `etype`;
CREATE TABLE `etype` (
  `typeId` int(11) NOT NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`typeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of etype
-- ----------------------------
INSERT INTO `etype` VALUES ('0', '管理员');
INSERT INTO `etype` VALUES ('1', '医生');
INSERT INTO `etype` VALUES ('2', '医院主管');
INSERT INTO `etype` VALUES ('3', '采购人员');
INSERT INTO `etype` VALUES ('4', '药房出药人员');
INSERT INTO `etype` VALUES ('5', '科室主任');
INSERT INTO `etype` VALUES ('6', '药房收费人员');

-- ----------------------------
-- Table structure for `medicinal`
-- ----------------------------
DROP TABLE IF EXISTS `medicinal`;
CREATE TABLE `medicinal` (
  `mid` varchar(25) NOT NULL COMMENT '药品id',
  `name` varchar(15) default NULL COMMENT '药品名称',
  `function` varchar(50) default NULL COMMENT '药品功能',
  `shellPrice` double default NULL COMMENT '售价',
  `bidPrice` double default NULL COMMENT '进价',
  `counterId` varchar(15) default NULL COMMENT '柜台id',
  `counterLeft` int(4) default NULL COMMENT '柜台库存',
  `repertoryId` varchar(15) default NULL COMMENT '仓库id',
  `repertoryLeft` int(4) default NULL COMMENT '仓库库存',
  `totalCount` int(11) default NULL,
  `isSoldOut` int(11) default NULL COMMENT '是否下架',
  `productTime` date NOT NULL default '0000-00-00' COMMENT '生产日期',
  `saveTime` int(2) default NULL COMMENT '保质期',
  `addTime` varchar(30) default NULL COMMENT '添加时间',
  `endTime` date default NULL COMMENT '到期时间',
  `updateTime` varchar(30) default NULL COMMENT '更新时间',
  PRIMARY KEY  (`mid`,`productTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of medicinal
-- ----------------------------
INSERT INTO `medicinal` VALUES ('10001', '风湿寒痛片', '风湿寒痛', '15', '10', '1', '0', '1', '9', '24', '0', '2019-05-09', '3', '2019-03-27 00:00:00', '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10002', '牛黄化毒片', '清热解毒', '15', '10', '2', '6', '2', '20', '33', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10003', '板蓝根颗粒', '预防感冒', '15', '10', '3', '20', '3', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10004', '利胆排石片', '胆结石', '15', '10', '4', '20', '4', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10005', '双黄连颗粒', '双黄连颗', '25', '10', '5', '20', '5', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10006', '香砂养胃丸', '养胃', '13', '10', '6', '8', '6', '20', '34', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10007', '金银花露', '保养', '25', '10', '7', '20', '7', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10008', '乌鸡白凤丸', '乌鸡白凤', '16', '10', '8', '20', '8', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10009', '牛黄清心丸', '牛黄清心', '48', '10', '9', '19', '9', '20', '39', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10010', '牛黄解毒丸', '牛黄解毒', '25', '10', '10', '20', '10', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10011', '止咳化痰丸', '止咳化痰', '36', '10', '11', '20', '11', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10012', '小儿回春丸', '小儿回春', '23', '10', '12', '20', '12', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10013', '六味地黄丸', '六味地黄', '20', '10', '13', '20', '13', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10014', '羚羊感冒片', '治疗感冒', '21.5', '15', '14', '2', '14', '20', '31', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10015', '润肌皮肤膏', '润肤', '22', '10', '15', '20', '15', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10016', '鸡苏丸', '鸡苏', '26', '10', '16', '20', '16', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10017', '香砂丸', '退热', '25', '10', '17', '20', '17', '20', '40', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10018', '鸡苏散', '鸡苏', '28', '10', '18', '20', '18', '20', '30', '1', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10019', '加味保和丸', '保养', '48', '10', '19', '20', '19', '20', '40', '1', '2019-03-07', '3', null, '2019-06-07', null);
INSERT INTO `medicinal` VALUES ('10019', '加味保和丸', '保养', '48', '10', '25', '20', '25', '20', '40', '0', '2019-05-15', '3', '2019-05-15 14:35:47', '2019-07-03', '2019-05-15 14:35:47');
INSERT INTO `medicinal` VALUES ('10020', '胃病丸', '胃病', '56', '10', '20', '20', '20', '20', '40', '0', '2019-03-07', '3', null, '2019-06-07', null);
INSERT INTO `medicinal` VALUES ('10021', '骨刺丸', '骨刺', '15', '10', '21', '20', '21', '20', '40', '0', '2019-03-07', '3', null, '2019-06-07', null);
INSERT INTO `medicinal` VALUES ('10022', '舒骨丸', '鸡苏', '13', '10', '25', '10', '22', '10', '20', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10023', '养心安神丸', '养心安神', '15', '10', '23', '10', '23', '10', '20', '0', '2019-05-09', '3', null, '2019-08-09', null);
INSERT INTO `medicinal` VALUES ('10024', '舒肝止痛丸', '舒肝止痛', '26', '10', '24', '10', '24', '10', '20', '0', '2019-05-09', '3', null, '2019-08-09', null);

-- ----------------------------
-- Table structure for `prescript`
-- ----------------------------
DROP TABLE IF EXISTS `prescript`;
CREATE TABLE `prescript` (
  `pid` varchar(50) NOT NULL COMMENT '药方id',
  `isCharge` int(1) default NULL COMMENT '是否收费',
  `isShell` int(1) default NULL COMMENT '是否出药',
  `isCancel` int(1) default NULL COMMENT '是否作废',
  `totalprice` double default NULL COMMENT '总价格',
  `typeId` int(11) default NULL COMMENT '科室id',
  `create_time` varchar(30) default NULL COMMENT '创建时间',
  `charge_time` varchar(30) default NULL COMMENT '收费时间',
  `shell_time` varchar(30) default NULL COMMENT '出药时间',
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of prescript
-- ----------------------------
INSERT INTO `prescript` VALUES ('1557905231057', '1', '1', '0', '601.5', '3', '2019-05-15 15:27:11', '2019-05-15 15:27:34', '2019-05-18 08:21:26');
INSERT INTO `prescript` VALUES ('1557905424740', '1', '0', '1', '200', '3', '2019-05-05 15:30:24', '2019-05-05 15:30:24', null);
INSERT INTO `prescript` VALUES ('1557905552537', '1', '0', '0', '280', '3', '2019-05-15 15:32:32', '2019-05-15 15:32:32', null);
INSERT INTO `prescript` VALUES ('1558158476744', '1', '1', '0', '63', '3', '2019-05-18 13:47:56', '2019-05-18 13:49:00', '2019-05-18 13:49:58');

-- ----------------------------
-- Table structure for `prescriptinfo`
-- ----------------------------
DROP TABLE IF EXISTS `prescriptinfo`;
CREATE TABLE `prescriptinfo` (
  `pid` varchar(50) NOT NULL COMMENT '药方id',
  `mid` varchar(25) NOT NULL default '' COMMENT '药品id',
  `mnum` int(4) default NULL COMMENT '药品数量',
  `mprice` double(11,0) default NULL COMMENT '药品价格',
  `total` double default NULL COMMENT '总价',
  PRIMARY KEY  (`pid`,`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of prescriptinfo
-- ----------------------------
INSERT INTO `prescriptinfo` VALUES ('1557905231057', '10001', '15', '15', '225');
INSERT INTO `prescriptinfo` VALUES ('1557905231057', '10002', '7', '15', '105');
INSERT INTO `prescriptinfo` VALUES ('1557905231057', '10006', '6', '13', '78');
INSERT INTO `prescriptinfo` VALUES ('1557905231057', '10014', '9', '22', '193.5');
INSERT INTO `prescriptinfo` VALUES ('1557905424740', '10003', '5', '15', '75');
INSERT INTO `prescriptinfo` VALUES ('1557905424740', '10005', '5', '25', '125');
INSERT INTO `prescriptinfo` VALUES ('1557905552537', '10018', '10', '28', '280');
INSERT INTO `prescriptinfo` VALUES ('1558158476744', '10001', '1', '15', '15');
INSERT INTO `prescriptinfo` VALUES ('1558158476744', '10009', '1', '48', '48');

-- ----------------------------
-- Table structure for `purchase`
-- ----------------------------
DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `pid` varchar(15) NOT NULL COMMENT '采购流水id',
  `wId` varchar(15) default NULL COMMENT '采购人员id',
  `isPut` int(1) default NULL COMMENT '是否入库',
  `isAccess` int(1) default NULL COMMENT '是否审核',
  `accessor` varchar(15) character set utf8 collate utf8_bin default NULL COMMENT '审核人',
  `applicant` varchar(15) default NULL COMMENT '申请人',
  `status` varchar(15) default NULL COMMENT '采购单状态',
  `createTime` varchar(30) default NULL COMMENT '创建时间',
  PRIMARY KEY  (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of purchase
-- ----------------------------
INSERT INTO `purchase` VALUES ('1557984998636', null, '0', '1', '10003', '10008', '1', '2019-05-16 13:36:38');

-- ----------------------------
-- Table structure for `purchaseinfo`
-- ----------------------------
DROP TABLE IF EXISTS `purchaseinfo`;
CREATE TABLE `purchaseinfo` (
  `pid` varchar(15) default NULL COMMENT '采购流水id',
  `mid` varchar(15) default '' COMMENT '药品id',
  `wid` varchar(15) default '',
  `newMedName` varchar(50) default NULL,
  `mnum` int(4) default NULL COMMENT '采购数量',
  `isPut` int(11) default NULL COMMENT '是否入库'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of purchaseinfo
-- ----------------------------
INSERT INTO `purchaseinfo` VALUES ('1557984998636', '10019', '10008', null, '10', '0');
INSERT INTO `purchaseinfo` VALUES ('1557984998636', '10020', '10008', null, '10', '0');
INSERT INTO `purchaseinfo` VALUES ('1557984998636', '10021', '10008', null, '10', '0');
INSERT INTO `purchaseinfo` VALUES ('', '10019', '10008', null, '10', '0');
INSERT INTO `purchaseinfo` VALUES ('', '10020', '10008', null, '10', '0');
INSERT INTO `purchaseinfo` VALUES ('', '10021', '10008', null, '10', '0');

-- ----------------------------
-- Table structure for `repertory`
-- ----------------------------
DROP TABLE IF EXISTS `repertory`;
CREATE TABLE `repertory` (
  `rid` int(15) NOT NULL auto_increment COMMENT '仓库id',
  `position` varchar(15) default NULL COMMENT '位置',
  `isUse` int(11) default NULL COMMENT '是否使用',
  PRIMARY KEY  (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of repertory
-- ----------------------------
INSERT INTO `repertory` VALUES ('1', '仓库10001', '1');
INSERT INTO `repertory` VALUES ('2', '仓库10002', '1');
INSERT INTO `repertory` VALUES ('3', '仓库10003', '1');
INSERT INTO `repertory` VALUES ('4', '仓库10004', '1');
INSERT INTO `repertory` VALUES ('5', '仓库10005', '1');
INSERT INTO `repertory` VALUES ('6', '仓库10006', '1');
INSERT INTO `repertory` VALUES ('7', '仓库10007', '1');
INSERT INTO `repertory` VALUES ('8', '仓库10008', '1');
INSERT INTO `repertory` VALUES ('9', '仓库10009', '1');
INSERT INTO `repertory` VALUES ('10', '仓库10010', '1');
INSERT INTO `repertory` VALUES ('11', '仓库10011', '1');
INSERT INTO `repertory` VALUES ('12', '仓库10012', '1');
INSERT INTO `repertory` VALUES ('13', '仓库10013', '1');
INSERT INTO `repertory` VALUES ('14', '仓库10014', '1');
INSERT INTO `repertory` VALUES ('15', '仓库10015', '1');
INSERT INTO `repertory` VALUES ('16', '仓库10016', '1');
INSERT INTO `repertory` VALUES ('17', '仓库10017', '1');
INSERT INTO `repertory` VALUES ('18', '仓库10018', '1');
INSERT INTO `repertory` VALUES ('19', '仓库10019', '1');
INSERT INTO `repertory` VALUES ('20', '仓库10020', '1');
INSERT INTO `repertory` VALUES ('21', '仓库10021', '1');
INSERT INTO `repertory` VALUES ('22', '仓库10022', '1');
INSERT INTO `repertory` VALUES ('23', '仓库10023', '1');
INSERT INTO `repertory` VALUES ('24', '仓库10024', '1');
INSERT INTO `repertory` VALUES ('25', '仓库10025', '1');
INSERT INTO `repertory` VALUES ('26', '仓库10026', '1');
INSERT INTO `repertory` VALUES ('27', '仓库10027', '0');
INSERT INTO `repertory` VALUES ('28', '仓库10028', '0');
INSERT INTO `repertory` VALUES ('29', '仓库10029', '0');
INSERT INTO `repertory` VALUES ('30', '仓库10030', '0');
INSERT INTO `repertory` VALUES ('31', '仓库10031', '0');
INSERT INTO `repertory` VALUES ('32', '仓库10032', '0');
INSERT INTO `repertory` VALUES ('33', '仓库10033', '0');
