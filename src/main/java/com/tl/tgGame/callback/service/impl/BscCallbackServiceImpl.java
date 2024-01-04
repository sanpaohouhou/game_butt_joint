package com.tl.tgGame.callback.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.tl.tgGame.callback.entity.BscBep20Tx;
import com.tl.tgGame.callback.entity.ChainEnum;
import com.tl.tgGame.callback.entity.TxContractDTO;
import com.tl.tgGame.callback.service.AbstractCallbackService;
import com.tl.tgGame.callback.service.BscBep20TxService;
import com.tl.tgGame.util.MapTool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BscCallbackServiceImpl extends AbstractCallbackService {
    @Resource
    private BscBep20TxService bscBep20TxService;


    @Override
    protected void dbTxContract(List<TxContractDTO> txContractList) {
        if (bscBep20TxService.lambdaQuery().eq(BscBep20Tx::getBlock, txContractList.get(0).getBlock()).count() > 0) {
            return;
        }
        List<BscBep20Tx> collect = txContractList.stream().map(txContractDTO -> (BscBep20Tx) txContractDTO).collect(Collectors.toList());
        bscBep20TxService.saveBatch(collect);
    }


    @Override
    public List<TxContractDTO> analysisTokenHandler(JsonArray token) {
        Iterator<JsonElement> iterator = token.iterator();
        List<TxContractDTO> list = new ArrayList<>();
        while (iterator.hasNext()) {
            BscBep20Tx bscBep20Tx = MapTool.toObject(iterator.next(), BscBep20Tx.class);
            bscBep20Tx.setCreateTime(bscBep20Tx.getCreateTime());
            bscBep20Tx.setContractAddress(bscBep20Tx.getContractAddress());
            bscBep20Tx.setHandled(false);
            list.add(bscBep20Tx);
        }
        return list;
    }

    @Override
    public ChainEnum chainNode() {
        return ChainEnum.BSC;
    }

}
