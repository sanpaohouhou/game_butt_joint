CREATE TABLE `currency`
(
    `id`      BIGINT         NOT NULL COMMENT '主键',
    `uid`     BIGINT         NOT NULL COMMENT '用户id',
    `type`    TINYINT(1) NOT NULL COMMENT '类型 0 用户余额 1 保证金',
    `balance` DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '总余额',
    `freeze`  DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '冻结余额',
    `remain`  DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '剩余余额',
    PRIMARY KEY (`id`),
    KEY       `idx_user_id` (`uid`),
    UNIQUE `uq_currency` (`uid`, `type`)
) COMMENT '用户余额表';

CREATE TABLE `currency_log`
(
    `id`          BIGINT         NOT NULL COMMENT '主键',
    `uid`         BIGINT         NOT NULL COMMENT '用户id',
    `type`        TINYINT(1) NOT NULL COMMENT '类型 0 用户余额 1 保证金',
    `sn`          VARCHAR(50)    NOT NULL COMMENT '订单号',
    `log_type`    VARCHAR(10)    NOT NULL COMMENT '记录类型',
    `des`         VARCHAR(20)    NOT NULL COMMENT '余额变动描述',
    `amount`      DECIMAL(16, 4) NOT NULL COMMENT '金额',
    `create_time` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `balance`     DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '总余额	',
    `freeze`      DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '冻结余额',
    `remain`      DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '剩余余额',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`uid`),
    KEY           `idx_sn` (`sn`)
) COMMENT '余额变动记录表';

CREATE TABLE `charge`
(
    `id`          BIGINT         NOT NULL COMMENT 'tid',
    `user_id`     BIGINT         NOT NULL COMMENT '用户id',
    `amount`      DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '金额',
    `charge_type` TINYINT(1) NOT NULL COMMENT '类型 0 充值 1 提现 2 结算',
    `type`        TINYINT(1) NOT NULL COMMENT '类型 0 用户余额 1 保证金',
    `address`     varchar(50)    NOT NULL COMMENT '地址',
    `network`     varchar(20)             DEFAULT NULL COMMENT '网络',
    `screen`      varchar(255)            DEFAULT NULL COMMENT '截图',
    `balance`     DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '总余额',
    `freeze`      DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '冻结余额',
    `remain`      DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '剩余余额',
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`user_id`)
) COMMENT ='用户充值提现记录表';


CREATE TABLE `user`
(
    `id`          BIGINT       NOT NULL COMMENT 'tg的id',
    `first_name`  VARCHAR(255) NOT NULL COMMENT '姓氏',
    `last_name`   VARCHAR(255)          DEFAULT NULL COMMENT '名字',
    `username`    VARCHAR(255)          DEFAULT NULL COMMENT 'tg的username',
    `invite_user` BIGINT                DEFAULT NULL COMMENT '谁邀请进群的',
    `joined_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次进群时间',
    `is_bot`      TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否是机器人',
    `partner_id`  BIGINT                DEFAULT NULL COMMENT '合伙人id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_username` (`username`),
    KEY           `idx_invite_user` (`invite_user`),
    KEY           `idx_partner_id` (`partner_id`)
) COMMENT ='用户表';

CREATE TABLE `config`
(
    `name`  varchar(255)  NOT NULL COMMENT 'key',
    `value` varchar(2048) NOT NULL COMMENT 'value',
    `note`  varchar(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`name`)
) COMMENT='配置表';

INSERT INTO `config` (`name`, `value`, `note`)
VALUES ('tg_bot_token', '6537817937:AAEr4vSYuWrrLGcBKlLGAuPm7WoYqb6iZ1A', '飞机机器人token');

INSERT INTO `config` (`name`, `value`, `note`)
VALUES ('tg_bot_name', 'shanpao_test_bot', '飞机机器人token');


INSERT INTO `config` (`name`, `value`, `note`)
VALUES ('tg_bot_token_two', '6686742791:AAGKb9ELt7Vykk_9ppRWe22kR0AW6Ggd-zk', '个人中心飞机机器人token');

INSERT INTO `config` (`name`, `value`, `note`)
VALUES ('tg_bot_name_two', 'heiheihei_test_bot', '个人中心飞机机器人名称');


alter table `user` add column `country` varchar(64) null comment '国家';
alter table `user` add column `game_account` varchar(64) null comment '游戏账号';

create table `bet`(
                      `id` bigint not null auto_increment comment '主键id',
                      `game_name` varchar(128) not null comment '游戏名称',
                      `amount` decimal(10,4) not null default  0.00 comment '下注金额',
                      `back_water_amount` decimal(10,4) not null default  0.00 comment '返水金额',
                      `top_commission` decimal(10,4) not null default 0.00 comment '上级返佣',
                      `all_back_water_amount` decimal(10,4) not null  default  0.00 comment '总返水（包含了未返水）',
                      `has_back_water` tinyint(1) not null default 0 comment '是否返水，0未返水，1已返水',
                      `user_id` bigint not null comment '用户id',
                      `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      `update_time` datetime null comment '更新时间',
                      primary key (`id`),
                      KEY `idx_user_id` (`user_id`)
) comment = '游戏下注表';


create table `game` (
                        `id` bigint not null auto_increment comment '主键id',
                        `game_name` varchar(128) not null comment '游戏名称',
                        `back_water_rate` decimal(10,4) not null default  0.00 comment '返水比例',
                        `top_commission_rate` decimal(10,4) not null default  0.02 comment '下级佣金比例',
                        primary key (`id`),
                        UNIQUE KEY `uq_game_name` (`game_name`)
) comment = '游戏名称';


CREATE TABLE `withdrawal`
(
    `id`            bigint         NOT NULL COMMENT '主键',
    `uid`           bigint         NOT NULL COMMENT '用户id',
    `hash`          varchar(80)             DEFAULT NULL COMMENT '交易hash',
    `network`       varchar(10)    NOT NULL COMMENT '交易网络',
    `from_address`  varchar(60)    NOT NULL COMMENT '发送地址',
    `to_address`    varchar(60)    NOT NULL COMMENT '充值地址',
    `amount`        decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '金额',
    `fee`           decimal(10, 6) NOT NULL DEFAULT '0.000000' COMMENT '手续费',
    `actual_amount` decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '实际到账',
    `status`        varchar(20)    NOT NULL DEFAULT '0' COMMENT '状态',
    `create_time`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `complete_time` datetime                DEFAULT NULL COMMENT '完成时间',
    `note`          varchar(1024)           DEFAULT NULL COMMENT '审核备注',
    `currency_type` varchar(10)    NOT NULL DEFAULT 'USER' COMMENT '提现类型',
    PRIMARY KEY (`id`),
    KEY `idx_hash` (`hash`),
    KEY `idx_to_address` (`to_address`),
    KEY `idx_uid` (`uid`)
) COMMENT ='u币提现表';

