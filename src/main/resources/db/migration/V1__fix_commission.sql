

alter table `user_commission` add column `bet_id` bigint null comment '下注记录id';

alter table `user_commission` add column `from_user_id` bigint null comment '来自用户id';

