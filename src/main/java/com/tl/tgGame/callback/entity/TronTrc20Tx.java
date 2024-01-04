package com.tl.tgGame.callback.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@EqualsAndHashCode(callSuper = true)
public class TronTrc20Tx extends TxContractDTO {
    private Boolean handled;
    private BigInteger netFee;
    private BigInteger energyFee;
    private long netUsage;
    private long energyUsage;
    private long originEnergyUsage;
    private long energyUsageTotal;
    private long status;
}
