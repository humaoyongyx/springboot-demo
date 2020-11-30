CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `lock_version` bigint(20) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `descp` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `no` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `lock_version` bigint(20) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `user_extra` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) DEFAULT NULL,
  `lock_version` bigint(20) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `descp` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `info` (
	`id` INT ( 11 ) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR ( 255 ) DEFAULT NULL,
	`descp` VARCHAR ( 255 ) DEFAULT NULL,
	`lock_version` BIGINT ( 20 ) DEFAULT NULL,
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY ( `id` )
) ENGINE = INNODB AUTO_INCREMENT = 14 DEFAULT CHARSET = utf8mb4;

-- 注意，在实际操作的时候，还是不要让jpa自动创建表

-- datetime不要设置精度，如datetime(6) 而是直接datetime

-- 创建日期和更新日期设置为 NOT NULL DEFAULT CURRENT_TIMESTAMP 和 datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP