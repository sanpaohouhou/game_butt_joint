package com.tl.tgGame.exception;


import com.tl.tgGame.common.dto.Response;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Created by wangqiyun on 2018/1/18.
 */
@RestControllerAdvice
public class HandleExceptionController {
//    @Resource
//    private MessageGetter messageGetter;

    @ExceptionHandler(value = {Exception.class})
    public Response resolveException(HttpServletRequest request, Exception e) {
        if (e instanceof APIException) {
//            return Response.error(((APIException) e).getErrcode(), "messageGetter.getOrDefault(e.getMessage(), e.getMessage(), ((APIException) e).getObjects())");
            return Response.error(((APIException) e).getErrcode(), e.getMessage());
        } else if (e instanceof ConstraintViolationException) {
            return Response.error("5023", e.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return Response.error("5024", e.getMessage());
        } else if (e instanceof MethodArgumentNotValidException || e instanceof HttpMessageNotReadableException || e instanceof IllegalArgumentException || e instanceof MissingServletRequestParameterException)
            return Response.error("5022", e.getMessage());
        else {
            e.printStackTrace();
//            return Response.error("9999", messageGetter.getOrDefault("error.INTERNAL_ERROR", "unknown error."));
            return Response.error("9999", ErrorEnum.SYSTEM_ERROR.getMessage());
        }
    }
}
