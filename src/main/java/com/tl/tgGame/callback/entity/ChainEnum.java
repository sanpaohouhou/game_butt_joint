package com.tl.tgGame.callback.entity;

import lombok.Getter;

@Getter
public enum ChainEnum {
    BSC(1_000),
    ETH(5_000),
//    OKEX(1_000),
    TRON(1_000);

    /**
     * 定时器间隔时间，单位毫秒
     */
    private final Integer time;

    ChainEnum(int time) {
        this.time = time;
    }
}
