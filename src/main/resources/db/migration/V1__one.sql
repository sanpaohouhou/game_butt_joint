

create table `user_extend`(
    `id` bigint not null auto_increment comment '主键id',
    `from_user_id` bigint not null comment '用户id',
    `to_user_id` bigint not null comment '被邀请用户id',
    `game_account` varchar(64) not null comment '游戏账号',
    `extend_url` varchar(255) not null comment '推广链接',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    primary key (`id`),
    UNIQUE KEY `uq_to_user_id` (`to_user_id`)
) comment = '用户推广表';

create table `user_profit` (
    `id` bigint not null auto_increment comment '主键id',
    `user_id` bigint not null  comment '用户id',
    `game_account` varchar(64) not null comment '游戏账号',
    `game_name` varchar(64) not null comment '游戏名称',
    `back_water_rate` decimal(10,4) not null default 0.00 comment '返水比例',
    `complete_back_water` decimal(10,4) not null default  0.00 comment '已返水',
    `wait_back_water` decimal(10,4) not null default 0.00 comment '待返水',
    `top_commission_rate` decimal(10,4) not null default 0.00 comment '上级佣金比例',
    `top_commission` decimal(10,4) not null default  0.00 comment '上级佣金',
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    primary key (`id`),
    UNIQUE KEY `uq_user_id_game_name` (`user_id`,`game_name`),
    KEY `idx_game_account` (`game_account`)
) comment = '用户获利表';



