create table `game_bet`
(

    `id`                bigint         not null auto_increment comment '主键id',
    `user_id`           bigint         not null comment '用户id',
    `game_id`           varchar(64)    not null comment '游戏id',
    `game_name`         varchar(128) null comment '游戏名',
    `bet`               decimal(10, 4) not null default 0.00 comment '下注金额',
    `game_business`     varchar(16)    not null comment '游戏商',
    `valid_bet`         decimal(10, 4) not null default 0.00 comment '有效下注',
    `profit`            decimal(10, 4) not null default 0.00 comment '盈利金额',
    `tax`               decimal(10, 4) not null default 0.00 comment '目前只有wl专用',
    `back_water_amount` decimal(10, 4) not null default 0.00 comment '待返水金额',
    `top_commission`    decimal(10, 4) not null default 0.00 comment '上级佣金',
    `record_id`         varchar(255)   not null default 0.00 comment '记录id,唯一',
    `game_account`      varchar(64)    not null comment '游戏账号',
    `has_settled`       tinyint(1) not null default 0 comment '是否结算,1结算,0未结算',
    `record_time`       datetime null comment '游戏方记录创建时间',
    `create_time`       datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime null comment '更新时间',
    primary key (`id`),
    UNIQUE KEY `unq_record_id` (`record_id`),
    KEY                 `inx_user_id` (`user_id`),
    KEY                 `inx_game_account` (`game_account`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户下注综合表';