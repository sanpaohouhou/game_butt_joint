package com.tl.tgGame.callback.service;


import com.tl.tgGame.callback.entity.ChainEnum;

public interface CallbackService {
    /**
     * 回调处理
     * @param str
     */
    void callbackHandler(String str);

    /**
     * 链节点
     * @return
     */
    ChainEnum chainNode();
}
