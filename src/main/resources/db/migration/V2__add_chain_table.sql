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

CREATE TABLE `address`
(
    `id`          bigint      NOT NULL,
    `uid`         bigint      NOT NULL COMMENT '用户id',
    `bsc`         varchar(60) NOT NULL COMMENT 'bsc地址',
    `eth`         varchar(60) NOT NULL COMMENT 'eth地址',
    `tron`        varchar(60) NOT NULL COMMENT 'tron地址',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_bsc` (`bsc`),
    UNIQUE KEY `uniq_eth` (`eth`),
    UNIQUE KEY `uniq_tron` (`tron`),
    KEY `idx_uid` (`uid`)
) COMMENT ='用户充值地址表';


CREATE TABLE `address_balance`
(
    `id`          bigint         NOT NULL COMMENT '主键',
    `uid`         bigint         NOT NULL COMMENT '用户id',
    `address`     varchar(60)    NOT NULL COMMENT '地址',
    `network`     varchar(10)    NOT NULL COMMENT '网络',
    `amount`      decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '余额',
    `status`      tinyint(1)     NOT NULL DEFAULT '0' COMMENT '状态值; 0:待归集, 1:正在归集',
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_address` (`address`, `network`),
    KEY `idx_address` (`address`),
    KEY `idx_uid` (`uid`)
) COMMENT ='地址余额';

CREATE TABLE `bsc_bep20_tx`
(
    `id`               bigint       NOT NULL COMMENT '主键',
    `hash`             varchar(80)  NOT NULL COMMENT '交易hash',
    `from`             varchar(60)  NOT NULL COMMENT 'from',
    `to`               varchar(60)  NOT NULL COMMENT 'to',
    `value`            varchar(255) NOT NULL COMMENT '值',
    `contract_address` varchar(60)  NOT NULL COMMENT '合约地址',
    `block`            bigint       NOT NULL COMMENT '区块',
    `create_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `handled`          tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否处理 0 未处理 1已处理',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_hash` (`hash`),
    KEY `idx_block` (`block`),
    KEY `idx_contract_address` (`contract_address`),
    KEY `idx_from` (`from`),
    KEY `idx_to` (`to`),
    KEY `idx_hash` (`hash`)
) COMMENT ='bsc转账tx表';

CREATE TABLE `eth_erc20_tx`
(
    `id`               bigint       NOT NULL COMMENT '主键',
    `hash`             varchar(80)  NOT NULL COMMENT '交易hash',
    `from`             varchar(60)  NOT NULL COMMENT 'from',
    `to`               varchar(60)  NOT NULL COMMENT 'to',
    `value`            varchar(255) NOT NULL COMMENT '值',
    `contract_address` varchar(60)  NOT NULL COMMENT '合约地址',
    `block`            bigint       NOT NULL COMMENT '区块',
    `create_time`      datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `handled`          tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否处理 0 未处理 1已处理',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_hash` (`hash`),
    KEY `idx_block` (`block`),
    KEY `idx_contract_address` (`contract_address`),
    KEY `idx_from` (`from`),
    KEY `idx_to` (`to`),
    KEY `idx_hash` (`hash`)
) COMMENT ='eth转账tx表';

CREATE TABLE `tron_trc20_tx`
(
    `id`                  bigint       NOT NULL COMMENT '主键',
    `hash`                varchar(80)  NOT NULL COMMENT '交易hash',
    `from`                varchar(60)  NOT NULL COMMENT 'from',
    `to`                  varchar(60)  NOT NULL COMMENT 'to',
    `value`               varchar(255) NOT NULL COMMENT '值',
    `contract_address`    varchar(60)  NOT NULL COMMENT '合约地址',
    `block`               bigint       NOT NULL COMMENT '区块',
    `create_time`         datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `handled`             tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否处理 0 未处理 1已处理',
    `net_fee`             bigint                DEFAULT NULL COMMENT '带宽费用',
    `energy_fee`          bigint                DEFAULT NULL COMMENT '能量费用',
    `net_usage`           bigint                DEFAULT NULL COMMENT '消耗带宽',
    `energy_usage`        bigint                DEFAULT NULL COMMENT '消耗用户能量',
    `origin_energy_usage` bigint                DEFAULT NULL COMMENT '消耗合约所有者能量',
    `energy_usage_total`  bigint                DEFAULT NULL COMMENT '消耗总能量',
    `status`              tinyint(1)            DEFAULT NULL COMMENT '状态 1成功 其他都是失败',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_hash` (`hash`),
    KEY `idx_block` (`block`),
    KEY `idx_contract_address` (`contract_address`),
    KEY `idx_from` (`from`),
    KEY `idx_to` (`to`),
    KEY `idx_hash` (`hash`)
) COMMENT ='tron转账tx表';

CREATE TABLE `usdt_withdrawal`
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

CREATE TABLE `heat_purse_record`
(
    `id`           BIGINT         NOT NULL COMMENT '主键id',
    `uid`          BIGINT         NOT NULL COMMENT 'uid',
    `sn`           VARCHAR(255)            DEFAULT NULL COMMENT '订单号',
    `network`      VARCHAR(32)    NOT NULL COMMENT '网络',
    `amount`       DECIMAL(16, 4) NOT NULL DEFAULT 0.00 COMMENT '金额',
    `from_address` VARCHAR(255)            DEFAULT NULL COMMENT '转出地址',
    `to_address`   VARCHAR(255)            DEFAULT NULL COMMENT '转入地址',
    `hash`         VARCHAR(255)            DEFAULT NULL COMMENT '交易hash值',
    `record_type`  VARCHAR(32)             DEFAULT NULL COMMENT '操作类型',
    `create_time`  datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `remark`       VARCHAR(255)   NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) COMMENT = '热钱包充提操作表';


CREATE TABLE `recovery_log`
(
    `id`          bigint         NOT NULL COMMENT 'id',
    `hash`        varchar(80)    NOT NULL COMMENT '交易hash',
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `network`     varchar(10)    NOT NULL COMMENT '网络',
    `to`          varchar(60)    NOT NULL COMMENT '接收地址',
    `amount`      decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '总数额',
    `status`      tinyint(1)     NOT NULL COMMENT '状态值; 0:待归集, 1:正在归集, 2:归集成功, 3:归集失败',
    PRIMARY KEY (`id`),
    KEY `idx_hash` (`hash`),
    UNIQUE `uq_hash` (`hash`),
    KEY `idx_to_address` (`to`)
) COMMENT ='归集日志表';


CREATE TABLE `recovery_log_detail`
(
    `id`          bigint         NOT NULL COMMENT 'id',
    `hash`        varchar(80)    NOT NULL COMMENT '交易hash',
    `create_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `address`     varchar(60)    NOT NULL COMMENT '保证金地址',
    `network`     varchar(10)    NOT NULL COMMENT '网络',
    `amount`      decimal(20, 6) NOT NULL DEFAULT '0.000000' COMMENT '数额',
    PRIMARY KEY (`id`),
    KEY `idx_hash` (`hash`),
    KEY `idx_address` (`address`)
) COMMENT ='归集日志表详情';
