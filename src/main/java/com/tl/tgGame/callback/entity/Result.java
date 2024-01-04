package com.tl.tgGame.callback.entity;

import lombok.Data;

@Data
public class Result<T> {
    private String code;
    private String msg;
    private String enMsg;
    private long time;
    private T data;
}
