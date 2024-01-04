package com.tl.tgGame.callback.req;

import com.tl.tgGame.callback.entity.ChainEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TxConditionReq {
    private String from;
    private String to;
    private String contractAddress;
    private ChainEnum chain;
}
