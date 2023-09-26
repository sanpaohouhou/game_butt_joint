
alter table `currency_game_profit` add column `settled` decimal(10,4) not null default 0.00 comment '结算总金额';

alter table `currency_game_profit` change column `game_type` `game_business` varchar(32) null comment '游戏商';