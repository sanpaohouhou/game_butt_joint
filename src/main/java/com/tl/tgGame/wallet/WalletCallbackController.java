package com.tl.tgGame.wallet;


import com.tl.tgGame.wallet.dto.NotifyDTO;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/wallet/callback")
public class WalletCallbackController {

    @Value("${wallet.appid}")
    private String appId;
    @Value("${wallet.secret}")
    private String secret;

    @Resource
    private WalletHandleService walletHandleService;

    @PostMapping("/message")
    public Response walletCB(@RequestBody NotifyDTO notifyDTO, @RequestHeader("appid") String appId,
                             @RequestHeader("sign") String sign, @RequestHeader("nonce") String nonce,
                             @RequestHeader("timestamp") String timestamp) {
        String str = String.join("_", appId, secret, timestamp, nonce);
        if (!DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8)).equalsIgnoreCase(sign)) {
            ErrorEnum.INTERNAL_ERROR.throwException();
        }
        walletHandleService.handleNotify(notifyDTO);
        return Response.success();
    }
}
