
alter table `user` add column  `tg_id` bigint default null comment 'tg的id';

alter table `user` add column `tg_group` varchar(255) default null comment 'tg的群组id';

ALTER TABLE `user`  ADD UNIQUE `uq_tg_id_group` (`tg_id`,`tg_group`);

alter table `user` add unique  `uq_game_account` (`game_account`);
