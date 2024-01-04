package com.tl.tgGame.callback.service;


import com.tl.tgGame.callback.entity.TronTrc20Tx;
import com.tl.tgGame.callback.entity.TronTxResult;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TxDetailService {
    @Resource
    private ConfigService configService;
    @Resource
    private UnirestInstance unirest;

    public List<TronTrc20Tx> getTxDetail(String chainType, String txid) {
        String url = configService.getOrDefault(ConfigConstants.TX_DETAIL_ADDRESS, "https://nft-data-center.assure.pro/api/tx/txDetail");
        HttpResponse<TronTxResult> response = unirest.get(url)
                .queryString("chainType", chainType)
                .queryString("txhash", txid).asObject(TronTxResult.class);
        TronTxResult result = response.getBody();

        if (result.getCode().equals("0")) {
            return result.getData();
        }
        return null;
    }
}
