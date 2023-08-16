package com.tl.tgGame.auth.resolver;


import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.util.ApplicationContextTool;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UidMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Uid.class) && (parameter.getParameterType().isAssignableFrom(Integer.class) || parameter.getParameterType().isAssignableFrom(Long.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory factory) throws Exception {
        AuthTokenService tokenService = ApplicationContextTool.getBean(AuthTokenService.class);
        assert tokenService != null;
        return tokenService.uid();
    }
}