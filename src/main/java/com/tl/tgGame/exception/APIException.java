package com.tl.tgGame.exception;


public class APIException extends RuntimeException {

    public APIException(String errcode, String message) {
        super(message);
        this.errcode = errcode;
        this.objects = null;
    }

    public APIException(String errcode, String message, Object[] objects) {
        super(message);
        this.errcode = errcode;
        this.objects = objects;
    }

    private final String errcode;
    private final Object[] objects;

    public String getErrcode() {
        return this.errcode;
    }

    public Object[] getObjects() {return this.objects; }
}
