package com.tl.tgGame.callback.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.tl.tgGame.callback.entity.ChainEnum;
import com.tl.tgGame.callback.entity.EthErc20Tx;
import com.tl.tgGame.callback.entity.TxContractDTO;
import com.tl.tgGame.callback.service.AbstractCallbackService;
import com.tl.tgGame.callback.service.EthErc20TxService;
import com.tl.tgGame.util.MapTool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EthCallbackServiceImpl extends AbstractCallbackService {
    @Resource
    private EthErc20TxService ethErc20TxService;
    @Override
    public ChainEnum chainNode() {
        return ChainEnum.ETH;
    }

    @Override
    protected void dbTxContract(List<TxContractDTO> txContractList) {
        if (ethErc20TxService.lambdaQuery().eq(EthErc20Tx::getBlock,txContractList.get(0).getBlock()).count() > 0) {
            return;
        }
        List<EthErc20Tx> collect = txContractList.stream().map(txContractDTO -> (EthErc20Tx) txContractDTO).collect(Collectors.toList());
        ethErc20TxService.saveBatch(collect);
    }


    @Override
    public List<TxContractDTO> analysisTokenHandler(JsonArray token) {
        Iterator<JsonElement> iterator = token.iterator();
        List<TxContractDTO> list = new ArrayList<>();
        while (iterator.hasNext()) {
            EthErc20Tx ethErc20Tx = MapTool.toObject(iterator.next(), EthErc20Tx.class);
            ethErc20Tx.setCreateTime(ethErc20Tx.getCreateTime());
            ethErc20Tx.setContractAddress(ethErc20Tx.getContractAddress());
            ethErc20Tx.setHandled(false);
            list.add(ethErc20Tx);
        }
        return list;
    }
}
