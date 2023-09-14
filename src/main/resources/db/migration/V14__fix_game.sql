
alter table `game` add column `status` tinyint(1) not null default 1 comment '1开启,0未开启';
alter table `game` add column `create_time` datetime not null default current_timestamp comment '创建时间';
alter table `game` add column `update_time` datetime null comment '更新时间';

insert into `game` (`game_name`,`back_water_rate`,`top_commission_rate`,`status`) values ('FC电子','0.002','0.008','1');
insert into `game` (`game_name`,`back_water_rate`,`top_commission_rate`,`status`) values ('WL电子','0.002','0.008','1');
insert into `game` (`game_name`,`back_water_rate`,`top_commission_rate`,`status`) values ('EG电子','0.002','0.008','1');
insert into `game` (`game_name`,`back_water_rate`,`top_commission_rate`,`status`) values ('WL百家乐','0.002','0.008','1');
insert into `game` (`game_name`,`back_water_rate`,`top_commission_rate`,`status`) values ('WL体育','0.002','0.008','1');
insert into `game` (`game_name`,`back_water_rate`,`top_commission_rate`,`status`) values ('FC捕鱼','0.002','0.008','1');