
alter table `user` add column `play_game_type` varchar(128) null comment '用户玩过哪些游戏';

INSERT INTO `config`
VALUES ('ag_jdb_host', 'http://389e.gf2-test.gfclub.site', 'ag_jdb域名');

INSERT INTO `config`
VALUES ('ag_jdb_operator_token', '77c4b98d6f5ae14e9ba24abe1dff0d34', 'ag_jdb请求token');

INSERT INTO `config`
VALUES ('ag_jdb_secret_key', 'd1d94fdfee9408152404ef4ba5fcac18', 'ag_jdb密钥key');


