package com.tl.tgGame.callback.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EthErc20Tx extends TxContractDTO {
    private Boolean handled;
}
