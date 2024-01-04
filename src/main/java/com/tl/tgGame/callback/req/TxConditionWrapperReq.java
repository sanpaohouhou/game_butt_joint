package com.tl.tgGame.callback.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TxConditionWrapperReq {
    private String callbackAddress;
    private List<TxConditionReq> txConditionReqs;
}
