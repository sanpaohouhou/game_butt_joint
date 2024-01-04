package com.tl.tgGame.blockchain;


import com.tl.tgGame.blockchain.bsc.BscBlockChainActuator;
import com.tl.tgGame.blockchain.eth.EthBlockChainActuator;
import com.tl.tgGame.callback.entity.TronTrc20Tx;
import com.tl.tgGame.callback.service.TxDetailService;
import com.tl.tgGame.project.enums.Network;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Service
public class TransactionDetailAdapter {
    @Resource
    private BscBlockChainActuator bscBlockChainActuator;
    @Resource
    private EthBlockChainActuator ethBlockChainActuator;
    @Resource
    private TxDetailService txDetailService;

    /**
     * 获取上链结果，0未上链 1成功 2失败
     *
     * @param network  网络
     * @param tx       hash
     * @param consumer consumer
     * @return 结果
     */
    public Integer transactionResultByHash(Network network, String tx, Consumer<Integer> consumer) {
        int result = 0;
        switch (network) {
            case TRC20:
                List<TronTrc20Tx> txTronList = txDetailService.getTxDetail("TRON", tx);
                if (txTronList != null && !txTronList.isEmpty()) {
                    if (txTronList.get(0).getStatus() == 1) {
                        result = 1;
                    } else {
                        result = 2;

                    }
                }
                break;
            case BEP20:
                TransactionResult transactionBscResult = bscBlockChainActuator.roughJudgmentTransactionByHash(tx);
                if (Objects.nonNull(transactionBscResult) && Objects.nonNull(transactionBscResult.getSuccess())) {
                    if (transactionBscResult.getSuccess()) {
                        result = 1;
                    } else {
                        result = 2;
                    }
                }
                break;
            case ERC20:
                TransactionResult transactionEthResult = ethBlockChainActuator.roughJudgmentTransactionByHash(tx);
                if (Objects.nonNull(transactionEthResult) && Objects.nonNull(transactionEthResult.getSuccess())) {
                    if (transactionEthResult.getSuccess()) {
                        result = 1;
                    } else {
                        result = 2;
                    }
                }
                break;
        }
        consumer.accept(result);
        return result;
    }
}
