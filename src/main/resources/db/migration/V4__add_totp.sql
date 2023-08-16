
CREATE TABLE `totp_secret`
(
    `id`     BIGINT       NOT NULL COMMENT '主键',
    `secret` VARCHAR(255) NOT NULL COMMENT '秘钥',
    `logged` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否登录过',
    PRIMARY KEY (`id`)
) COMMENT ='totp秘钥';