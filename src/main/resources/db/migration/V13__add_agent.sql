create table `agent`
(
    `id`            bigint         not null auto_increment comment '主键id',
    `agent_name`    varchar(128)   not null comment '代理商姓名',
    `user_name`     varchar(128)   not null comment '用户名:登录账号',
    `mobile`        varchar(32)    null comment '手机号',
    `dividend_rate` decimal(10, 4) not null default 0.00 comment '固定占成比例',
    `invite_id`     bigint null comment '上级邀请代理商id',
    `invite_chain`  varchar(255)   not null comment '代理商邀请链',
    `password`      varchar(128)   not null comment '密码',
    `enabled`       tinyint(1) not null default 1 comment '是否可用,1可用,0不可用',
    `has_deleted`   tinyint(1) not null default 0 comment '是否删除,1删除,0未删除',
    `level`         int(5) not null default 1 comment '代理商级别,1,2',
    `remark`        varchar(255) null comment '备注',
    `user_id`       bigint         not null comment '用户id(和user表打通)',
    `game_account`  varchar(128)   not null comment '游戏账号',
    `create_time`   datetime       not null default current_timestamp comment '创建时间',
    `update_time`   datetime null comment '更新时间',
    primary key (`id`),
    UNIQUE KEY `unq_agent_name` (`agent_name`),
    UNIQUE KEY `unq_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理商表';

alter table `user`
    add column `has_agent` tinyint(1) not null default 0 comment '是否是代理,1是,0不是';

alter table `user` add column `agent_id` bigint null comment '代理商id,和agent打通';