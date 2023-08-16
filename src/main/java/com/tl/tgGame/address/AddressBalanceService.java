package com.tl.tgGame.address;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.address.entity.Address;
import com.tl.tgGame.address.entity.AddressBalance;
import com.tl.tgGame.address.mapper.AddressBalanceMapper;
import com.tl.tgGame.blockchain.UsdtContractAdapter;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.enums.Network;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 地址余额 服务实现类
 * </p>
 *
 * @author hd
 * @since 2022-05-26
 */
@Service
public class AddressBalanceService extends ServiceImpl<AddressBalanceMapper, AddressBalance> {
    @Resource
    private AddressService addressService;
    @Resource
    private UsdtContractAdapter usdtContractAdapter;

    public boolean updateBalance(String address, Network network, BigDecimal balance) {
        return update(new LambdaUpdateWrapper<AddressBalance>().eq(AddressBalance::getAddress, address).eq(AddressBalance::getNetwork, network).set(AddressBalance::getAmount, balance).set(AddressBalance::getUpdateTime, LocalDateTime.now()));
    }

    public void syncAddressBalance(String address, Network network) {
        AddressBalance addressBalance = lambdaQuery()
                .eq(AddressBalance::getAddress, address)
                .eq(AddressBalance::getNetwork, network).one();
        // 新增一个
        if (Objects.isNull(addressBalance)) {
            Address userAddress = null;
            switch (network) {
                case ERC20:
                    userAddress = addressService.getByEthAddress(address);
                    break;
                case BEP20:
                    userAddress = addressService.getByBscAddress(address);
                    break;
                case TRC20:
                    userAddress = addressService.getByTronAddress(address);
                    break;
            }
            if (Objects.isNull(userAddress)) return;
            boolean save = save(AddressBalance.builder()
                    .uid(userAddress.getUid())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .address(address)
                    .network(network)
                    .amount(BigDecimal.ZERO)
                    .status(0)
                    .build());
            if (!save) ErrorEnum.SYSTEM_ERROR.throwException();
        }
        BigDecimal balance = usdtContractAdapter.getAddressBalance(address, network);
        updateBalance(address, network, balance);
    }

    public void asyncAddressBalance(String address, Network network) {
        ASYNC_UPDATE_ADDRESS_BALANCE_POOL.execute(() -> {
            syncAddressBalance(address, network);
        });
    }


    private static final ThreadPoolExecutor ASYNC_UPDATE_ADDRESS_BALANCE_POOL = new ThreadPoolExecutor(1, 8, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>(64));
}
