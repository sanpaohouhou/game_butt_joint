
INSERT INTO `config`
VALUES ('fc_agent_code', 'XM809', 'FC密钥code');

INSERT INTO `config`
VALUES ('fc_host', 'https://api.fcg666.net', 'fc请求域名');

INSERT INTO `config`
VALUES ('fc_agent_key', 'rK8dho7YOHoUUKy8', 'fc请求渠道key');

INSERT INTO `config`
VALUES ('eg_agent_code', '389bet_cny', 'eg密钥code');

INSERT INTO `config`
VALUES ('eg_host', 'https://txmulti.api.helloholyfa.com/api/v1/', 'eg请求域名');

INSERT INTO `config`
VALUES ('eg_hash_key', 'pEd9heiqDR', 'eg_hmac_sha256_key');

INSERT INTO `config`
VALUES ('eg_platform', '389bet', 'eg平台value');


INSERT INTO `config`
VALUES ('wl_host', 'https://www.walitest.com:7021/api/', 'wl请求域名');

INSERT INTO `config`
VALUES ('wl_param_key', 'jtG8QtGWt+tKMFYrPRuTmWNz9oKGWoVb3+rjiO2SySI=', 'wl参数加密key');

INSERT INTO `config`
VALUES ('wl_req_key', 'EDV3NVzRBmTXjujVMZbxDA==', 'wl请求加密key');

INSERT INTO `config`
VALUES ('wl_account', 'vu5i0', 'wl-account');

INSERT INTO `config`
    VALUE ('eg_game_list','[{"gameId":"witchlove","name":"魔女炼爱","image":"https://tg-game-dev.s3.ap-northeast-1.amazonaws.com/image/%E9%AD%94%E5%A5%B3%E7%85%89%E6%84%9B.png","multiple":"1000000","onlineNumber":"1907"},
{"gameId":"nekomaid","name":"甜心女仆","image":"https://tg-game-dev.s3.ap-northeast-1.amazonaws.com/image/%E7%94%9C%E5%BF%83%E5%A5%B3%E5%83%95.png","multiple":"25000","onlineNumber":"1687"},{"gameId":"adventureofsinbad","name":"辛巴达冒险","image":"https://tg-game-dev.s3.ap-northeast1.amazonaws.com/image/%E8%BE%9B%E5%B7%B4%E9%81%94%E5%86%92%E9%9A%AA.png","multiple":"25000","onlineNumber":"1532"},{"gameId":"sicbovideo","name":"​​骰宝黄金轮","image":"https://tg-game-dev.s3.ap-northeast1.amazonaws.com/image/%E9%AA%B0%E5%AF%B6%E9%BB%83%E9%87%91%E8%BC%AA.png","multiple":"50000","onlineNumber":"1732"},{"gameId":"xocdiavideo","name":"​色碟黄金轮","image":"https://tg-game-dev.s3.ap-northeast1.amazonaws.com/image/%E8%89%B2%E7%A2%9F%E9%BB%83%E9%87%91%E8%BC%AA.png","multiple":"1000000","onlineNumber":"1845"},{"gameId":"hooheyhowvideo","name":"鱼虾蟹黄金轮","image":"https://tg-game-dev.s3.ap-northeast1.amazonaws.com/image/%E9%AD%9A%E8%9D%A6%E8%9F%B9%E9%BB%83%E9%87%91%E8%BC%AA.png","multiple":"200000","onlineNumber":"1849"},{"gameId":"blastxp","name":"BlastX","image":"https://tg-game-dev.s3.ap-northeast-1.amazonaws.com/image/BlastX.png","multiple":"1000000","onlineNumber":"1907"},{"gameId":"plinkop",
"name":"Plinko","image":"https://tg-game-dev.s3.ap-northeast1.amazonaws.com/image/Plinko.png","multiple":"250000","onlineNumber":"1702"},{"gameId":"hilop","name":"HiLo",
"image":"https://tg-game-dev.s3.ap-northeast-1.amazonaws.com/image/HiLo.png","multiple":"60000","onlineNumber":"2305"},
{"gameId":"wheelp","name":"Wheel","image":"https://tg-game-dev.s3.ap-northeast-1.amazonaws.com/image/Wheel.png","multiple":"80000","onlineNumber":"3606"},{"gameId":"dicep","name":"Dice",
"image":"https://tg-game-dev.s3.ap-northeast-1.amazonaws.com/image/Dice.png","multiple":"25000","onlineNumber":"6300"}]
','eg游戏列表');

INSERT INTO `config`
VALUES ('wl_agent_id', '31665', 'wl-渠道id');

INSERT INTO `config`
VALUES ('wl_game_usdt_point', '7', 'wl转换汇率,默认按照7');

alter table `user_commission` modify `game_id` varchar(128) null comment '游戏id';

alter table `wl_bet` add column `update_time` datetime DEFAULT NULL COMMENT '更新时间';

alter table `wl_bet` add column `back_water_amount` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '返水金额';

alter table `wl_bet` add column `top_commission` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '上级返佣金额';

INSERT INTO `config`
VALUES ('person_center_bot_url', 'https://t.me/first_new_one_bot', '个人中心机器人链接');

INSERT INTO `config`
VALUES ('exclusion_customer_service', 'https://t.me/cin89886', '机器人唯一专属客服跳转链接');

INSERT INTO `config`
VALUES ('bot_group_invite_link', 'https://t.me/+OG9aPdVgn2wyN2Z', '邀请链接');

INSERT INTO `config`
VALUES ('tg_game_h5_url', 'http://ec2-54-168-241-36.ap-northeast-1.compute.amazonaws.com/#/home', '邀请链接');


INSERT INTO `config`(`name`, `value`, `note`) VALUES ('s3_AWSAccessKeyId', 'AKIAXO26WTLFNTM4T2NE', 'S3配置信息');
INSERT INTO `config`(`name`, `value`, `note`) VALUES ('s3_AWSSecretAccessKey', '1ueYr+Iow0nj3VtObNZWbCgxKIQaSZAeRPBB8jq1Yl66LylEvmOzQytjjyR3nyQV', 'S3配置信息');
INSERT INTO `config`(`name`, `value`, `note`) VALUES ('s3_bucketName', 'tg-game-jp', 'S3配置信息');
INSERT INTO `config`(`name`, `value`, `note`) VALUES ('s3_region', 'ap-northeast-1', 'S3配置信息');
INSERT INTO `config`(`name`, `value`, `note`) VALUES ('s3_url', 'https://tg-game-jp.s3-accelerate.amazonaws.com', 'S3配置信息');


INSERT INTO `config`(`name`, `value`, `note`) VALUES ('game_back_water_rate', '0.002', '游戏返水比例');


INSERT INTO `config`
VALUES ('bot_begin_game_group_link', 'https://t.me/+OG9aPdVgn2wyN2Z', '机器人开始游戏群链接');




