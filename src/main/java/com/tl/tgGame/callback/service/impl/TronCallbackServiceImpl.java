package com.tl.tgGame.callback.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.tl.tgGame.callback.entity.ChainEnum;
import com.tl.tgGame.callback.entity.TronTrc20Tx;
import com.tl.tgGame.callback.entity.TxContractDTO;
import com.tl.tgGame.callback.service.AbstractCallbackService;
import com.tl.tgGame.callback.service.TronTrc20TxService;
import com.tl.tgGame.util.MapTool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TronCallbackServiceImpl extends AbstractCallbackService {

    @Resource
    private TronTrc20TxService tronTrc20TxService;



    @Override
    protected void dbTxContract(List<TxContractDTO> txContractList) {
        if (tronTrc20TxService.lambdaQuery().eq(TronTrc20Tx::getBlock,txContractList.get(0).getBlock()).count() > 0) {
            return;
        }
        List<TronTrc20Tx> collect = txContractList.stream().map(txContractDTO -> (TronTrc20Tx) txContractDTO).collect(Collectors.toList());
        tronTrc20TxService.saveBatch(collect);
    }


    @Override
    public List<TxContractDTO> analysisTokenHandler(JsonArray token) {
        Iterator<JsonElement> iterator = token.iterator();
        List<TxContractDTO> list = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonObject next = iterator.next().getAsJsonObject();
            TronTrc20Tx tronTrc20Tx = MapTool.toObject(next, TronTrc20Tx.class);
            tronTrc20Tx.setHandled(false);
            tronTrc20Tx.setEnergyFee(tronTrc20Tx.getEnergyFee());
            tronTrc20Tx.setNetFee(tronTrc20Tx.getNetFee());
            tronTrc20Tx.setNetUsage(tronTrc20Tx.getNetUsage());
            tronTrc20Tx.setOriginEnergyUsage(tronTrc20Tx.getOriginEnergyUsage());
            tronTrc20Tx.setEnergyUsageTotal(tronTrc20Tx.getEnergyUsageTotal());
            tronTrc20Tx.setEnergyUsage(tronTrc20Tx.getEnergyUsage());
            tronTrc20Tx.setCreateTime(tronTrc20Tx.getCreateTime());
            tronTrc20Tx.setContractAddress(tronTrc20Tx.getContractAddress());
            list.add(tronTrc20Tx);
        }
        return list;
    }

    @Override
    public ChainEnum chainNode() {
        return ChainEnum.TRON;
    }

}
