package com.tl.tgGame.callback.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TronTxResult extends Result<List<TronTrc20Tx>>{
}
