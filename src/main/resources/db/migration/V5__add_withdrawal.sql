CREATE TABLE `withdrawal`
(
    `id`            bigint         NOT NULL COMMENT '主键',
    `uid`           bigint         NOT NULL COMMENT '用户id',
    `hash`          varchar(80)             DEFAULT NULL COMMENT '交易hash',
    `network`       varchar(10)    NOT NULL COMMENT '交易网络',
    `from_address`  varchar(60)             DEFAULT NULL COMMENT '发送地址',
    `to_address`    varchar(60)             DEFAULT NULL COMMENT '充值地址',
    `amount`        decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '金额',
    `fee`           decimal(10, 6) NOT NULL DEFAULT '0.000000' COMMENT '手续费',
    `actual_amount` decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '实际到账',
    `status`        varchar(20)    NOT NULL DEFAULT 'created' COMMENT '状态',
    `create_time`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `complete_time` datetime                DEFAULT NULL COMMENT '完成时间',
    `note`          varchar(1024)           DEFAULT NULL COMMENT '审核备注',
    `user_type`     varchar(20)             DEFAULT NULL COMMENT '用户类型',
    `screen`        varchar(255)            DEFAULT NULL COMMENT '截图',
    PRIMARY KEY (`id`),
    KEY             `idx_hash` (`hash`),
    KEY             `idx_to_address` (`to_address`),
    KEY             `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='u币提现表';