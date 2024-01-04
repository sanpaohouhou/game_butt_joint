package com.tl.tgGame.callback.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tl.tgGame.callback.entity.TxContractDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class AbstractCallbackService implements CallbackService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void callbackHandler(String str) {
        JsonObject jsonObject = new Gson().fromJson(str, JsonObject.class);
        // 代币
        JsonArray token = jsonObject.get("token").getAsJsonArray();
        List<TxContractDTO> txContractList = analysisTokenHandler(token);

        // 落库操作
        Optional.ofNullable(txContractList).ifPresent(this::dbTxContract);
    }

//    protected abstract JsonObject deserialization(String str);


    /**
     * 落库操作
     * @param txContractList
     * @return
     */
    protected abstract void dbTxContract(List<TxContractDTO> txContractList);


    /**
     * 分析json转换为 TxContractDTO 实体
     * @param token
     * @return
     */
    public abstract List<TxContractDTO> analysisTokenHandler(JsonArray token);

}
