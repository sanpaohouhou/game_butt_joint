package com.tl.tgGame.wallet.dto;


import lombok.Data;

@Data
public class Response {
    private String code = "0";
    private String message = "success";

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public Response() {
    }

    public static Response success(){
        return new Response();
    }


    public static Response error(String code, String message) {
        return new Response(code, message);
    }

    public boolean isSuccess(){
        return code.equalsIgnoreCase("0");
    }
}
