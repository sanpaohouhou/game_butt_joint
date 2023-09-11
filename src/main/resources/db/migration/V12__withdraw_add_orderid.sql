alter table `withdrawal`
    add column `order_id` bigint null comment '提现系统订单id',
    ADD INDEX `idx_order_id` (`order_id`),
    ADD UNIQUE INDEX `uq_order_id` (`order_id`);
