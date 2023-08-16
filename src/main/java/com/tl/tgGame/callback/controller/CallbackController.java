package com.tl.tgGame.callback.controller;


import com.tl.tgGame.address.AddressService;
import com.tl.tgGame.address.entity.Address;
import com.tl.tgGame.callback.entity.*;
import com.tl.tgGame.callback.req.TxConditionReq;
import com.tl.tgGame.callback.req.TxConditionWrapperReq;
import com.tl.tgGame.callback.service.*;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestInstance;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/callback")
@Log4j2
public class CallbackController implements ApplicationRunner {
    public static final String NONCE = "VtZq0j9aOVDNEhd5EelX_HlKVBpns43UP0Acoprwp3x81FaFP7O5m6CM79iBGLb";
    @Resource
    private ConfigService configService;
    @Resource
    private UnirestInstance unirest;
    @Resource
    private List<CallbackService> callbackServiceList;
    @Resource
    private AddressService addressService;
    @Resource
    private TxDetailService txDetailService;
    @Resource
    private BscBep20TxService bscBep20TxService;
    @Resource
    private TronTrc20TxService tronTrc20TxService;
    @Resource
    private EthErc20TxService ethErc20TxService;

    private static final Map<ChainEnum, CallbackService> callbackServiceMap = new ConcurrentHashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        callbackServiceList.forEach(callbackService -> callbackServiceMap.put(callbackService.chainNode(), callbackService));
        String callbackAddress = configService.get(ConfigConstants.TX_CALLBACK);
        System.out.println(callbackAddress);
        String url = configService.getOrDefault(ConfigConstants.REGISTER_ADDRESS, "https://nft-data-center.assure.pro/api/push/callbackAddress/register");
        for (int i = 0; i < 3; i++) {
            HttpResponse<Result> response = unirest.post(url).field("callbackAddress", callbackAddress).asObject(Result.class);
            Result data = response.getBody();
            log.info("注册数据中心: {}", data);
            if (response.getStatus() == 200 && data.getCode().equals("0")) {
                List<Address> addresses = addressService.list();
                String bep = configService.get(ConfigConstants.USDT_BEP20_CONTRACT);
                String trc = configService.get(ConfigConstants.USDT_TRC20_CONTRACT);
                String erc = configService.get(ConfigConstants.USDT_ERC20_CONTRACT);
                List<TxConditionReq> list = new ArrayList<>();
                addresses.forEach(it -> {
                    list.add(TxConditionReq.builder().chain(ChainEnum.BSC).contractAddress(bep).to(it.getBsc()).build());
                    list.add(TxConditionReq.builder().chain(ChainEnum.TRON).contractAddress(trc).to(it.getTron()).build());
                    list.add(TxConditionReq.builder().chain(ChainEnum.ETH).contractAddress(erc).to(it.getEth()).build());
                });
                pushCondition(TxConditionWrapperReq.builder()
                        .callbackAddress(callbackAddress)
                        .txConditionReqs(list).build());
                break;
            }
            TimeUnit.SECONDS.sleep(3);
        }
    }
    @RequestMapping(value = {"/message/{nonce}", "/message", "/message/{nonce}/{chain}"}, produces = "text/plain")
    public String message(@PathVariable(required = false) ChainEnum chain, @PathVariable(required = false) String nonce, @RequestBody(required = false) String str) {
        if (chain == null || (StringUtils.isNotEmpty(nonce) && !nonce.equalsIgnoreCase(NONCE))) { //等于ping
            return "success";
        }
        try {
            log.info("数据中心回调: {}", str);
//            String jsonString = new Gson().fromJson(str, String.class);
            callbackServiceMap.get(chain).callbackHandler(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
        return "success";
    }

    @PostMapping("/push/condition")
    public Response pushCondition(@RequestBody TxConditionWrapperReq req) {
        String url = configService.getOrDefault(ConfigConstants.CONDITION_ADDRESS, "https://nft-data-center.assure.pro/api/push/condition");
        HttpResponse<Result> response = unirest.post(url).contentType("application/json").body(req).asObject(Result.class);
        Result data = response.getBody();
        log.info("数据中心推送: {}", data);
        return new Response(data.getCode(), data.getMsg(), data.getData());
    }

    @GetMapping("/sync")
    public Response txSync(@RequestParam String hash, @RequestParam ChainEnum chainType) {
        List<TronTrc20Tx> txList = txDetailService.getTxDetail(chainType.name(), hash);
        txList.forEach(it -> {
            try {
                switch (chainType) {
                    case BSC:
                        BscBep20Tx bscBep20Tx = new BscBep20Tx();
                        BeanUtils.copyProperties(it, bscBep20Tx);
                        bscBep20Tx.setHandled(false);
                        bscBep20TxService.save(bscBep20Tx);
                    case ETH:
                        EthErc20Tx ethErc20Tx = new EthErc20Tx();
                        BeanUtils.copyProperties(it, ethErc20Tx);
                        ethErc20Tx.setHandled(false);
                        ethErc20TxService.save(ethErc20Tx);
                    case TRON:
                        it.setHandled(false);
                        tronTrc20TxService.save(it);
                }
            } catch (Exception ignored) {
            }
        });
        return Response.success();
    }
}
