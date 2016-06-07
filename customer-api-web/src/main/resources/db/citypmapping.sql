CREATE TABLE `city_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL COMMENT '城市编码(乐住)',
  `otacode` varchar(50) NOT NULL COMMENT '城市编码（ota）',
  `otatype` bigint(10) NOT NULL COMMENT 'ota类型',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`) USING BTREE,
  KEY `idx_otacode` (`otacode`) USING BTREE,
  KEY `idx_otatype` (`otatype`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1  DEFAULT CHARSET=utf8 COMMENT='城市映射表';

