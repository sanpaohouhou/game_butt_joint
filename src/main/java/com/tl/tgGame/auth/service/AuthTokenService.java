package com.tl.tgGame.auth.service;

public interface AuthTokenService {

    /**
     * 获取登录的id 未登录返回null
     * @return id
     */
    Long _uid();

    String token();

    /**
     * 获取登录的id 未登录返回null
     * @return id
     */
    Long _uid(String token);

    /**
     * 获取登录的id 未登录返回错误
     * @return id
     */
    Long uid();

    /**
     * 获取登录token
     * @param uid 登录Id
     * @return token
     */
    String login(Long uid);
}
