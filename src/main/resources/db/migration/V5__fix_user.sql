
alter table `user` add column `invite_chain` varchar(255) not null comment '邀请链关系,上级:自己';
