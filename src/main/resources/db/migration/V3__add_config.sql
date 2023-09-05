

INSERT INTO `config`
VALUES ('wl_game_usdt_point', '7', 'wl转换汇率,默认按照7');

alter table `user_commission` modify `game_id` varchar(128) null comment '游戏id';

alter table `wl_bet` add column `update_time` datetime DEFAULT NULL COMMENT '更新时间';

alter table `wl_bet` add column `back_water_amount` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '返水金额';

alter table `wl_bet` add column `top_commission` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '上级返佣金额';