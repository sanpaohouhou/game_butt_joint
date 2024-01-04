package com.tl.tgGame.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {
    private String code = "0";
    private String message = "success";
    private Object data;

    public Response(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(Object data) {
        this.data = data;
    }

    public static Response success() {
        return new Response(null);
    }
    public static Response success(Object data) {
        return new Response(data);
    }

    public static Response pageResult(IPage<?> page){
        return Response.success(PageObject.of(page));
    }

    public static Response error(String code, String message) {
        return new Response(code, message);
    }
}
