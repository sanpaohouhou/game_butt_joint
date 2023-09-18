package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.blockchain.UsdtContractAdapter;
import com.tl.tgGame.common.lock.RedisLock;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Agent;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.Wallet;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.mapper.WithdrawalMapper;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.tgBot.service.BotMessageService;
import com.tl.tgGame.util.CompareUtil;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.util.TimeUtil;
import com.tl.tgGame.wallet.WalletAPI;
import com.tl.tgGame.wallet.dto.SingleResponse;
import com.tl.tgGame.wallet.dto.UserUsdtWithdrawDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

@Service
public class WithdrawalServiceImpl extends ServiceImpl<WithdrawalMapper, Withdrawal> implements WithdrawalService {
    @Resource
    private ConfigService configService;
    @Resource
    private RedisLock redisLock;
    @Resource
    private RedisKeyGenerator redisKeyGenerator;
    @Resource
    private CurrencyService currencyService;
    @Resource
    private WalletService walletService;
    @Resource
    private UsdtContractAdapter usdtContractAdapter;
    @Resource
    private DefaultIdentifierGenerator defaultIdentifierGenerator;

    @Autowired
    private WithdrawalMapper withdrawalMapper;

    @Resource
    private WalletAPI walletAPI;
    @Resource
    private BotMessageService botMessageService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private UserService userService;

    @Override
    public Withdrawal addWithdrawal(Long uid, BigDecimal amount, UserType userType, String fromAddress, String toAddress, String hash, Network network, String screen, String note) {
        long id = defaultIdentifierGenerator.nextId(null);
        Withdrawal withdrawal = Withdrawal.builder()
                .id(id)
                .uid(uid)
                .toAddress(toAddress)
                .fromAddress(fromAddress)
                .amount(amount)
                .fee(BigDecimal.ZERO)
                .actualAmount(amount)
                .hash(hash)
                .screen(screen)
                .network(network)
                .status(WithdrawStatus.withdraw_success)
                .note(note)
                .createTime(LocalDateTime.now())
                .completeTime(LocalDateTime.now())
                .userType(userType)
                .build();
        save(withdrawal);
        return withdrawal;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Withdrawal withdraw(long uid, UserType userType, Network network, String to, BigDecimal amount) {
        String key = redisKeyGenerator.generateKey("withdraw", userType, uid);
        redisLock.redissonLock(key);
        try {
            Currency currency = currencyService.getOrCreate(uid, userType);
            if (CompareUtil.isLessThan(amount, BigDecimal.TEN)) {
                ErrorEnum.WITHDRAW_FAIL.throwException("提现金额必须大于10U");
            }
            if (amount.compareTo(currency.getRemain()) > 0) {
                ErrorEnum.CREDIT_LACK.throwException();
            }
            BigDecimal fixedFee = BigDecimal.ZERO;
            switch (network) {
                case TRC20:
                    fixedFee = configService.getDecimal(ConfigConstants.TRC20_USDT_WITHDRAWAL_FIXED_FEE);
                    break;
                case BEP20:
                    fixedFee = configService.getDecimal(ConfigConstants.BEP20_USDT_WITHDRAWAL_FIXED_FEE);
                    break;
                case ERC20:
                    fixedFee = configService.getDecimal(ConfigConstants.ERC20_USDT_WITHDRAWAL_FIXED_FEE);
                    break;
            }
            if (!CompareUtil.isGreaterThan(amount.subtract(fixedFee), BigDecimal.ZERO)) {
                ErrorEnum.WITHDRAW_FAIL.throwException("提现金额必须大于手续费");
            }
            WithdrawStatus status = WithdrawStatus.created;
            Wallet wallet = walletService.getNormalWallet();

            String fromAddress = null;
            if (network.equals(Network.TRC20)) {
                fromAddress = wallet.getTronAddress();
            } else {
                fromAddress = wallet.getEthAddress();
            }
            BigDecimal actualAmount = amount.subtract(fixedFee);
            long id = defaultIdentifierGenerator.nextId(null);
            Withdrawal withdrawal = Withdrawal.builder()
                    .id(id)
                    .uid(uid)
                    .createTime(LocalDateTime.now())
                    .toAddress(to)
                    .fromAddress(fromAddress)
                    .amount(amount)
                    .fee(fixedFee)
                    .actualAmount(actualAmount)
                    .network(network)
                    .status(status)
                    .userType(userType)
                    .build();
            boolean saved = save(withdrawal);
            if (!saved) {
                ErrorEnum.WITHDRAW_FAIL.throwException("提现失败");
            }
            // 冻结余额
            currencyService.freeze(uid, userType, BusinessEnum.WITHDRAW, amount, id, "保证金提现预冻结");
            return withdrawal;
        } finally {
            redisLock.redissonUnLock();
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Withdrawal audit(long id, boolean result, String note) {
        String key = redisKeyGenerator.generateKey("audit", id);
        redisLock.redissonLock(key);
        try {
            Withdrawal withdrawal = getById(id);
            if (Objects.isNull(withdrawal)) {
                ErrorEnum.OBJECT_NOT_FOUND.throwException("订单不存在");
            }
            if (!withdrawal.getStatus().equals(WithdrawStatus.created)) {
                ErrorEnum.ORDER_EXCEPTION.throwException();
            }
            List<InlineKeyboardButton> keyboardButtons = Collections.singletonList(InlineKeyboardButton.builder().text("唯一充提财务").url("https://t.me/cin89886").build());

            if (result) {
                withdrawal.setStatus(WithdrawStatus.review_success);
                UserUsdtWithdrawDTO userUsdtWithdrawDTO = new UserUsdtWithdrawDTO();
                userUsdtWithdrawDTO.setNetwork(withdrawal.getNetwork());
                userUsdtWithdrawDTO.setTo(withdrawal.getToAddress());
                userUsdtWithdrawDTO.setAmount(withdrawal.getActualAmount());
                userUsdtWithdrawDTO.setUid(withdrawal.getUid());

                SingleResponse<Withdrawal> response = walletAPI.withdraw(userUsdtWithdrawDTO);
                if (!response.isSuccess()) {
                    ErrorEnum.throwException(response.getCode(), response.getMessage());
                }
                Withdrawal responseData = response.getData();
                withdrawal.setOrderId(responseData.getId());

                if(!userService.getById(withdrawal.getUid()).getHasAgent()){
                    botMessageService.sendMessage2UserAsync(withdrawal.getUid(), "提现审核成功，打款中~~~",
                            InlineKeyboardMarkup.builder().keyboardRow(keyboardButtons).build());
                }
            } else {
                withdrawal.setStatus(WithdrawStatus.review_fail);
                withdrawal.setCompleteTime(LocalDateTime.now());
                currencyService.unfreeze(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "保证金提现订单审核失败解冻");
                if(!userService.getById(withdrawal.getUid()).getHasAgent()) {
                    botMessageService.sendMessage2UserAsync(withdrawal.getUid(), "提现审核失败，原因: " + note,
                            InlineKeyboardMarkup.builder().keyboardRow(keyboardButtons).build());
                }
            }
            withdrawal.setNote(note);
            updateById(withdrawal);
            return withdrawal;
        } finally {
            redisLock.redissonUnLock();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Withdrawal upload(Long id, String image, String hash) {
        String key = redisKeyGenerator.generateKey("upload", id);
        redisLock.redissonLock(key);
        try {
            Withdrawal withdrawal = getById(id);
            if (Objects.isNull(withdrawal)) {
                ErrorEnum.OBJECT_NOT_FOUND.throwException("订单不存在");
            }
            if (!withdrawal.getStatus().equals(WithdrawStatus.review_success)) {
                ErrorEnum.ORDER_EXCEPTION.throwException();
            }
            withdrawal.setStatus(WithdrawStatus.withdraw_success);
            withdrawal.setScreen(image);
            withdrawal.setHash(hash);
            updateById(withdrawal);
            currencyService.reduce(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "用户提现运营审核通过打款成功");
            return withdrawal;
        } finally {
            redisLock.redissonUnLock();
        }
    }

    @Override
    public BigDecimal todayWithdrawalAmount(long uid) {
        return (BigDecimal) getMap(new QueryWrapper<Withdrawal>().select("IFNULL(SUM(`amount`), 0) AS `total`").lambda()
                .eq(Withdrawal::getUid, uid)
                .gt(Withdrawal::getCreateTime, TimeUtil.getTodayBegin())
                .notIn(Withdrawal::getStatus, Arrays.asList(WithdrawStatus.withdraw_fail, WithdrawStatus.review_fail))).get("total");
    }

    @Override
    public Integer todayUserWithdrawalCount(long uid) {
        return lambdaQuery().eq(Withdrawal::getUid, uid)
                .gt(Withdrawal::getCreateTime, TimeUtil.getTodayBegin())
                .notIn(Withdrawal::getStatus, Arrays.asList(WithdrawStatus.withdraw_fail, WithdrawStatus.review_fail)).count();

    }

    @Override
    public BigDecimal todayPlatformWithdrawalAmount() {
        return (BigDecimal) getMap(new QueryWrapper<Withdrawal>().select("IFNULL(SUM(`amount`), 0) AS `total`").lambda()
                .gt(Withdrawal::getCreateTime, TimeUtil.getTodayBegin())
                .notIn(Withdrawal::getStatus, Arrays.asList(WithdrawStatus.withdraw_fail, WithdrawStatus.review_fail))).get("total");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transfer(Withdrawal withdrawal) {
        Consumer<String> consumer = (tx) -> {
            withdrawal.setHash(tx);
            withdrawal.setStatus(WithdrawStatus.withdrawing);
            updateById(withdrawal);
        };
        usdtContractAdapter.transfer(withdrawal.getNetwork(), withdrawal.getToAddress(), withdrawal.getActualAmount(), consumer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferFail(Withdrawal withdrawal) {
        withdrawal.setStatus(WithdrawStatus.withdraw_fail);
        withdrawal.setCompleteTime(LocalDateTime.now());
        currencyService.unfreeze(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "保证金提现失败解冻");
        updateById(withdrawal);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferSuccess(Withdrawal withdrawal) {
        withdrawal.setStatus(WithdrawStatus.withdraw_success);
        withdrawal.setCompleteTime(LocalDateTime.now());
        updateById(withdrawal);
        currencyService.reduce(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "保证金提现");
    }

    @Override
    public BigDecimal allWithdrawalAmount(Long userId, UserType userType, List<WithdrawStatus> statuses, LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Withdrawal> wrapper =
                new LambdaQueryWrapper<Withdrawal>()
                        .eq(Withdrawal::getUid, userId)
                        .eq(Withdrawal::getUserType, userType)
                        .in(Withdrawal::getStatus, statuses)
                        .ge(Objects.nonNull(startTime), Withdrawal::getCreateTime, startTime)
                        .le(Objects.nonNull(endTime), Withdrawal::getCreateTime, endTime);
        ;
        return withdrawalMapper.sumWithdrawal(wrapper);
    }

    @Override
    public Integer countJuniorWithdrawalAmount(Long inviteUserId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper<Object> wrapper = new QueryWrapper<>().eq(Objects.nonNull(inviteUserId), "u.invite_user", inviteUserId)
                .eq(Objects.nonNull(userId), "wi.uid", userId)
                .ge(Objects.nonNull(startTime), "wi.create_time", startTime)
                .le(Objects.nonNull(endTime), "wi.create_time", endTime);
        return withdrawalMapper.countJuniorWithdrawalAmount(wrapper);
    }

    @Override
    public Page<Withdrawal> agentWithdrawalList(Integer page, Integer size, Long userId, Long agentId,
                                      Long id, WithdrawStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        page = (page - 1) * size;
        QueryWrapper<Object> wrapper = new QueryWrapper<>().eq("u.has_agent", true)
                .eq(Objects.nonNull(userId), "wi.uid", userId)
                .eq(Objects.nonNull(agentId), "u.agent_id", agentId)
                .eq(Objects.nonNull(id), "wi.id", id)
                .eq(Objects.nonNull(startTime), "wi.status", status)
                .ge(Objects.nonNull(startTime), "wi.create_time", startTime)
                .le(Objects.nonNull(endTime), "wi.create_time", endTime);

        QueryWrapper<Object> countWrapper = wrapper.orderByDesc("wi.id").last(" LIMIT " + page + "," + size);
        List<Withdrawal> withdrawals = withdrawalMapper.agentList(wrapper);
        Page<Withdrawal> page1 = new Page<>();
        page1.setPages(page);
        page1.setSize(size);
        if (CollectionUtils.isEmpty(withdrawals)) {
            return page1;
        }
        List<Withdrawal> list = new ArrayList<>();
        for (Withdrawal withdrawal : withdrawals) {
            Agent agent = agentService.queryByUserId(withdrawal.getUid());
            if(agent != null){
                withdrawal.setPAgentId(agent.getInviteId());
                withdrawal.setAgentId(agent.getId());
                withdrawal.setLevel(agent.getLevel());
            }
            list.add(withdrawal);
        }
        Integer total = withdrawalMapper.agentCount(countWrapper);
        page1.setRecords(list);
        page1.setTotal(total);
        return page1;
    }
}
