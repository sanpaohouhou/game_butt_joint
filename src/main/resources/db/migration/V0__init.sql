CREATE TABLE `currency`
(
    `id`        BIGINT         NOT NULL COMMENT '主键',
    `uid`       BIGINT         NOT NULL COMMENT '用户id',
    `user_type` varchar(20)    NOT NULL COMMENT '用户类型',
    `balance`   DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '总余额',
    `freeze`    DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '冻结余额',
    `remain`    DECIMAL(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '剩余余额',
    PRIMARY KEY (`id`),
    KEY         `idx_user_id` (`uid`),
    UNIQUE `uq_currency` (`uid`, `user_type`)
) COMMENT '用户余额表';

CREATE TABLE `currency_log`
(
    `id`          bigint         NOT NULL COMMENT '主键',
    `uid`         bigint         NOT NULL COMMENT '用户id',
    `sn`          varchar(50)    NOT NULL COMMENT '订单号',
    `log_type`    varchar(10)    NOT NULL COMMENT '记录类型',
    `des`         varchar(1024)           DEFAULT NULL COMMENT '余额变动描述',
    `amount`      decimal(16, 4) NOT NULL COMMENT '金额',
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `balance`     decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '总余额	',
    `freeze`      decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '冻结余额',
    `remain`      decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '剩余余额',
    `user_type`   varchar(20)             DEFAULT NULL COMMENT '用户类型',
    `business`    varchar(20)             DEFAULT NULL COMMENT '业务',
    PRIMARY KEY (`id`),
    KEY           `idx_user_id` (`uid`),
    KEY           `idx_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='余额变动记录表';


CREATE TABLE `user`
(
    `id`             BIGINT       NOT NULL COMMENT 'tg的id',
    `first_name`     VARCHAR(255) NOT NULL COMMENT '姓氏',
    `last_name`      VARCHAR(255)          DEFAULT NULL COMMENT '名字',
    `username`       VARCHAR(255)          DEFAULT NULL COMMENT 'tg的username',
    `invite_user`    BIGINT                DEFAULT NULL COMMENT '谁邀请进群的',
    `joined_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次进群时间',
    `is_bot`         TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否是机器人',
    `partner_id`     BIGINT                DEFAULT NULL COMMENT '合伙人id',
    `tg_id`          bigint                default null comment 'tg的id',
    `tg_group`       varchar(255)          default null comment 'tg的群组id',
    `game_account`   varchar(64) null comment '游戏账号',
    `country`        varchar(64) null comment '国家',
    `has_group`      tinyint(1) not null default 1 comment '是否在群组,1在 0不在',
    `withdrawal_url` varchar(64) null comment '提现地址',
    `has_join_eg`    tinyint(1) not null default 0 comment '是否注册过eg,1注册过,0未注册',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_username` (`username`),
    UNIQUE KEY `uq_tg_id` (`tg_id`),
    UNIQUE KEY `uq_game_account` (`game_account`),
    KEY              `idx_invite_user` (`invite_user`),
    KEY              `idx_partner_id` (`partner_id`)
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



create table `game`
(
    `id`                  bigint         not null auto_increment comment '主键id',
    `game_name`           varchar(128)   not null comment '游戏名称',
    `back_water_rate`     decimal(10, 4) not null default 0.00 comment '返水比例',
    `top_commission_rate` decimal(10, 4) not null default 0.02 comment '下级佣金比例',
    primary key (`id`),
    UNIQUE KEY `uq_game_name` (`game_name`)
) comment = '游戏名称';


INSERT INTO `config`
VALUES ('bsc_chain_id', '42', NULL);
INSERT INTO `config`
VALUES ('bsc_chain_url', 'https://kovan.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161', NULL);
INSERT INTO `config`
VALUES ('bsc_contract_address', '0x008819daAceDE27F4fF89c9b63529461901dAB7F', 'BSC链的归集充值合约地址');
INSERT INTO `config`
VALUES ('condition_address', 'https://nft-data-center.assure.pro/api/push/condition', NULL);
INSERT INTO `config`
VALUES ('eth_chain_id', '42', NULL);
INSERT INTO `config`
VALUES ('eth_chain_url', 'https://kovan.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161', NULL);
INSERT INTO `config`
VALUES ('eth_contract_address', '0x008819daAceDE27F4fF89c9b63529461901dAB7F', 'ETH链的归集充值合约地址');
INSERT INTO `config`
VALUES ('flat_money_withdrawal_daily_limit', '10000', '用户法币每日提现最大限制');
INSERT INTO `config`
VALUES ('flat_money_withdrawal_fee', '0.1', '用户法币提现比例');
INSERT INTO `config`
VALUES ('flat_money_withdrawal_small_amount', '10', '用户法币提现最小金额');
INSERT INTO `config`
VALUES ('manager_wallet',
        'OPF+GvcPMDAIkvzsESX3JXb1HL/iQrI4+fQPlIYsYYdpR54zNTUZ54J/JYXiOw4OR7LyF8rpnMadg6lbEcTPegoCBzVs6OzLo34IYFGn08ZpGlOccKUmF89RLTfy1pD4UhlmH5lxwMQWGbKz7+cvc9cdL9QdTA79yQBMsf4NthTi8mi6eT+QYGsPg3PGfZq7mdyg0882C9JjxRWIthRytcbhccHjZjRSXZeAbZHirSd3jp1V/kKLhmFEuIMuZGgJwfM8nnM76V4JvgS2/t8qSqZvdtLIGS0OySOiPMq8/cb2brUQBMbOAl/LEXZ24r5RpBriAkrqWTGwUWTW95VQfjQOA9g79RMmfZPJ55pwXHjFcDjYQEX9YkT5UdCP4WDc9SdtPau1IRvcnkRs3HnZVFDynQ6SG47D57WJihe7KoZV3OTe6WIwvzNoujE7YhOkSXNBgvN2HveH5tPYRH7PEeW9LhrP5N5SP+TBeYsOwCVjc/aChlqFW9/q44jtkski',
        '管理归集的钱包');
INSERT INTO `config`
VALUES ('normal_wallet',
        'OPF+GvcPMDAIkvzsESX3JXb1HL/iQrI4+fQPlIYsYYdpR54zNTUZ54J/JYXiOw4OR7LyF8rpnMadg6lbEcTPegoCBzVs6OzLo34IYFGn08ZpGlOccKUmF89RLTfy1pD4UhlmH5lxwMQWGbKz7+cvc9cdL9QdTA79yQBMsf4NthTi8mi6eT+QYGsPg3PGfZq7mdyg0882C9JjxRWIthRytcbhccHjZjRSXZeAbZHirSd3jp1V/kKLhmFEuIMuZGgJwfM8nnM76V4JvgS2/t8qSqZvdtLIGS0OySOiPMq8/cb2brUQBMbOAl/LEXZ24r5RpBriAkrqWTGwUWTW95VQfjQOA9g79RMmfZPJ55pwXHjFcDjYQEX9YkT5UdCP4WDc9SdtPau1IRvcnkRs3HnZVFDynQ6SG47D57WJihe7KoZV3OTe6WIwvzNoujE7YhOkSXNBgvN2HveH5tPYRH7PEeW9LhrP5N5SP+TBeYsOwCVjc/aChlqFW9/q44jtkski',
        '提现使用的钱包');
INSERT INTO `config`
VALUES ('register_address', 'https://nft-data-center.assure.pro/api/push/callbackAddress/register', NULL);
INSERT INTO `config`
VALUES ('tron_chain_url', 'grpc.nile.trongrid.io:50051', NULL);
INSERT INTO `config`
VALUES ('tron_contract_address', 'TGQSXaUfgXszx9nzwRjrr6Ub3bScUSnNhW', 'TRON链的归集充值合约地址');
INSERT INTO `config`
VALUES ('tx_callback', 'http://game.dyproapp.click/api/callback/message', NULL);
INSERT INTO `config`
VALUES ('tx_detail_address', 'https://nft-data-center.assure.pro/api/tx/txDetail', NULL);
INSERT INTO `config`
VALUES ('usdt_bep20_contract', '0x65be5459944B234386b98fd1BAAC97503d953874', NULL);
INSERT INTO `config`
VALUES ('usdt_bep20_decimals', '6', NULL);
INSERT INTO `config`
VALUES ('usdt_erc20_contract', '0x65be5459944B234386b98fd1BAAC97503d953874', NULL);
INSERT INTO `config`
VALUES ('usdt_trc20_contract', 'TXLAQ63Xg1NAzckPwKHvzw7CSEmLMEqcdj', NULL);
INSERT INTO `config`
VALUES ('bep20_usdt_withdrawal_fixed_fee', '1', 'bsc链提现手续费');
INSERT INTO `config`
VALUES ('erc20_usdt_withdrawal_fixed_fee', '5', 'eth链提现手续费');
INSERT INTO `config`
VALUES ('trc20_usdt_withdrawal_fixed_fee', '1', 'tron链提现手续费');
INSERT INTO `config`
VALUES ('auto_recycle', 'true', '是否开启自动归集');
INSERT INTO `config`
VALUES ('auto_recycle_amount', '1000', '归集金额触发值');


CREATE TABLE `totp_secret`
(
    `id`     BIGINT       NOT NULL COMMENT '主键',
    `secret` VARCHAR(255) NOT NULL COMMENT '秘钥',
    `logged` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否登录过',
    PRIMARY KEY (`id`)
) COMMENT ='totp秘钥';

CREATE TABLE `role`
(
    `id`              bigint      NOT NULL COMMENT 'id',
    `role_name`       varchar(50) NOT NULL COMMENT '角色名',
    `remark`          varchar(50) COMMENT '备注',
    `active`          tinyint(1) COMMENT '是否激活',
    `permission_code` text COMMENT '参数',
    `created_time`    bigint(20) NOT NULL DEFAULT '0' COMMENT '时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';


CREATE TABLE `admin`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `username`    VARCHAR(255) NOT NULL COMMENT '用户名',
    `password`    VARCHAR(255) NOT NULL COMMENT '密码',
    `note`        VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `enable`      tinyint(1) not null default 1 comment '',
    `role_id`     bigint comment '角色id',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    KEY           `idx_admin_username` (`username`)
) COMMENT ='管理员';

INSERT INTO `admin`(`id`, `username`, `password`, `note`, `create_time`, `enable`, `role_id`)
VALUES (1, 'admin', '{bcrypt}$2a$10$sUS/m5r//XHa3t7DLSr5/uXeSmxHAG.T5cKP80onlkUwSLYMGinbK', '', '2022-10-25 11:13:31',
        1, 1);


INSERT INTO `role`(`id`, `role_name`, `remark`, `active`, `permission_code`, `created_time`)
VALUES (1, '超级管理员', '1112', 1,
        'HOME,HOME_ONE,USER_MANAGE,USER_LIST,PARTNER_MANAGE,PARTNER_LIST,MARGIN_WITHDRAWAL,GAMBLING_MANAGE,RECORDER_LIST,CARD_TABLE_LIST,GROUP_LIST,BET_LIST,UPDATE_OPEN_CARD_RESULT,FINANCE_MANAGE,FINANCE_OPERATING_TABLE,FINANCE_DAILY_STATEMENT,AGGREGATION_MANAGE,HEAT_PURSE_RECORD,LIVE_MANAGE,LIVE_AUTH,LIVE_TYPE,LIVE_LIST,GIFT_LIST,LIVE_RECORD,GIFT_RECORD,EXCHANGE_RECORD',
        0);


CREATE TABLE `recharge`
(
    `id`           bigint         NOT NULL COMMENT 'tid',
    `user_id`      bigint         NOT NULL COMMENT '用户id',
    `amount`       decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '金额',
    `user_type`    varchar(20)             DEFAULT NULL COMMENT '用户类型',
    `from_address` varchar(50)             DEFAULT NULL COMMENT '转入地址',
    `network`      varchar(20)             DEFAULT NULL COMMENT '网络',
    `screen`       varchar(255)            DEFAULT NULL COMMENT '截图',
    `balance`      decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '总余额',
    `freeze`       decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '冻结余额',
    `remain`       decimal(16, 4) NOT NULL DEFAULT '0.0000' COMMENT '剩余余额',
    `create_time`  datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
    `to_address`   varchar(50)             DEFAULT NULL COMMENT '转出地址',
    `hash`         varchar(80)             DEFAULT NULL COMMENT '交易hash',
    `note`         varchar(1024)           DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_charge` (`network`,`hash`),
    KEY            `idx_user_id` (`user_id`),
    KEY            `idx_from_address` (`from_address`),
    KEY            `idx_to_address` (`to_address`),
    KEY            `idx_hash` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户充值提现记录表';


create table `user_commission`
(
    `id`            bigint         not null AUTO_INCREMENT comment '主键id',
    `user_id`       bigint         not null comment '用户id',
    `game_id`       bigint         not null default 0 comment '游戏id',
    `game_name`     varchar(64) null comment '游戏名称',
    `type`          varchar(32)    not null default 'COMMISSION' comment '利润类型,COMMISSION佣金,BACK_WATER返水',
    `profit`        decimal(10, 4) not null default 0.00 comment '获利金额',
    `rate`          decimal(10, 4) not null default 0.00 comment '获利比例',
    `actual_amount` decimal(10, 4) not null default 0.00 comment '实际分钱金额',
    `create_time`   datetime       not null DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
    `bet_id`        bigint null comment '下注记录id',
    `from_user_id`  bigint null comment '来自用户id',
    primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户获利记录表';

create table `currency_game_profit`
(
    `id`        bigint         not null comment '',
    `user_id`   bigint         not null comment '',
    `game_type` varchar(64)    not null default 'FC' comment '游戏商类型,FC,WL,EG',
    `balance`   decimal(10, 4) not null default 0.00 comment '账户余额',
    `freeze`    decimal(10, 4) not null default 0.00 comment '冻结金额',
    primary key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户玩各种游戏账户记录';

create table `bet`
(
    `id`                bigint         not null comment '',
    `record_id`         varchar(255)   not null comment '游戏编号(唯一)',
    `game_account`      varchar(64)    not null comment '游戏账户名',
    `game_id`           int(6) not null comment '游戏id',
    `game_name`         varchar(128) null comment '游戏名称',
    `game_type`         int(6) not null comment '游戏类型',
    `bet`               decimal(10, 4) not null default 0.00 comment '下注金额',
    `win_lose`          decimal(10, 4) not null default 0.00 comment '净输赢(winlose = prize - bet)',
    `prize`             decimal(10, 4) not null default 0.00 comment '赢分',
    `refund`            decimal(10, 4) not null default 0.00 comment '退换金额',
    `valid_bet`         decimal(10, 4) not null default 0.00 comment '有效投注',
    `commission`        decimal(10, 4) not null default 0.00 comment '抽水金额',
    `jp_mode`           int(6) null comment 'jackpot种类',
    `jp_points`         decimal(10, 4) not null default 0.00 comment 'jackpot中奖金额',
    `jp_tax`            decimal(10, 6) not null default 0.00 comment 'jackpot抽水(支持到小数第六位)',
    `befores`           decimal(10, 4) not null default 0.00 comment '下注前点数',
    `afters`            decimal(10, 4) not null default 0.00 comment '下注后点数',
    `b_date`            datetime       not null comment '建立事件',
    `is_buy_feature`    tinyint(1) not null default 0 comment '是否购买免费游戏',
    `user_id`           bigint         not null comment '用户id',
    `has_settled`       tinyint(1) not null default 0 comment '是否返水,0未返水,1已返水',
    `back_water_amount` decimal(10, 4)          default 0.00 comment '此笔订单返水金额',
    `top_commission`    decimal(10, 4)          default 0.00 comment '上级返佣金额',
    `create_time`       datetime       not null DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime null comment '更新时间',
    `pull_time`         datetime null comment '拉取记录时间(默认没有,每次拉取的时候作为时间,往后拉15分钟的记录)',
    primary key (`id`),
    unique key `uq_record_id` (`record_id`),
    key                 `idx_game_account` (`game_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户下注表';

create table `eg_bet`
(
    `id`                bigint         not null auto_increment comment '主键id',
    `user_id`           bigint         not null comment '用户id',
    `has_settled`       tinyint(1) not null default 0 comment '是否结算,1已结算,0未结算',
    `back_water_amount` decimal(10, 4) not null default 0.00 comment '返水金额',
    `top_commission`    decimal(10, 4) not null default 0.00 comment '上级返佣金额',
    `round_id`          varchar(128)   not null comment '对局id',
    `game_id`           varchar(32)    not null comment '游戏id',
    `game_name`         varchar(32) null comment '游戏名',
    `player_id`         varchar(64)    not null comment '玩家id(name)',
    `bet_time`          varchar(32)    not null comment '下注时间',
    `win_time`          varchar(32)    not null comment '赢时间',
    `wp_time`           varchar(32)    not null comment 'wp时间',
    `currency`          varchar(16)    not null comment '币种',
    `bet_type`          varchar(32)    not null comment '下注类型0:非对赌 1庄家 2闲家',
    `extra`             varchar(512)   not null comment '',
    `bet`               varchar(32)    not null comment '下注金额',
    `win`               varchar(32)    not null comment '赢的金额',
    `net_win`           varchar(32)    not null comment '实际输赢金额',
    `round_code`        varchar(32)    not null comment '对局code',
    `before_balance`    varchar(32)    not null comment '下注前余额',
    `after_balance`     varchar(32)    not null comment '下注后余额',
    `create_time`       datetime       not null default current_timestamp comment '创建时间',
    `update_time`       datetime null comment '更新时间',
    `pull_time`         datetime null comment '拉取记录时间',
    primary key (`id`),
    UNIQUE KEY `unq_round_id` (`round_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='eg游戏下注表';


CREATE TABLE `wl_bet`
(
    `id`              bigint         NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`         bigint         NOT NULL COMMENT '用户id',
    `game`            int            NOT NULL COMMENT '游戏类型',
    `category`        int            NOT NULL COMMENT '游戏分类',
    `profit`          decimal(10, 4) NOT NULL                                       DEFAULT '0.0000' COMMENT '系统实际盈利',
    `balance`         decimal(10, 4) NOT NULL                                       DEFAULT '0.0000' COMMENT '结算时，用户最新余额',
    `bet`             decimal(10, 4) NOT NULL                                       DEFAULT '0.0000' COMMENT '投注额',
    `valid_bet`       decimal(10, 4) NOT NULL                                       DEFAULT '0.0000' COMMENT '有效投注额',
    `tax`             decimal(10, 4) NOT NULL                                       DEFAULT '0.0000' COMMENT '游戏税收',
    `game_start_time` datetime       NOT NULL COMMENT '游戏开始时间',
    `record_time`     datetime       NOT NULL COMMENT '数据记录时间',
    `game_id`         varchar(64)    NOT NULL COMMENT '投注单号',
    `game_name`       varchar(32) NULL COMMENT '游戏名',
    `record_id`       varchar(64)    NOT NULL COMMENT '记录全局唯一的id',
    `detail_url`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '投注详情查询url',
    `create_time`     datetime       NOT NULL,
    `pull_time`       datetime       NOT NULL,
    `has_settled`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '该记录是否操作过分钱\n1操作过\n0未操作',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unq_record_id` (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4930 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='游戏记录表';
