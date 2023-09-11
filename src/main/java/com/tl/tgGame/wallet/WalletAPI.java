package com.tl.tgGame.wallet;

import com.tl.tgGame.address.entity.Address;
import com.tl.tgGame.wallet.dto.RechargeCheckDTO;
import com.tl.tgGame.wallet.dto.SingleResponse;
import com.tl.tgGame.wallet.dto.UserUsdtWithdrawDTO;
import com.tl.tgGame.project.entity.Withdrawal;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface WalletAPI {

    @RequestLine("GET /api/open/address/{uid}")
    SingleResponse<Address> getUserAddress(@Param("uid") Long uid);

    @RequestLine("GET /api/open/recharge/check")
    SingleResponse<Boolean> rechargeCheck(@QueryMap() RechargeCheckDTO rechargeCheckDTO);

    @RequestLine("POST /api/open/withdrawal/withdraw")
    SingleResponse<Withdrawal> withdraw(UserUsdtWithdrawDTO usdtWithdrawDTO);


}
