package com.tl.tgGame.callback.entity;

import lombok.Data;

@Data
public class TxContractDTO extends TxDTO {
    /**
     * 合约地址
     */
    private String contractAddress;

}
