package com.tl.tgGame.wallet;


import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.Recharge;
import com.tl.tgGame.project.entity.Withdrawal;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.WithdrawStatus;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.RechargeService;
import com.tl.tgGame.project.service.WithdrawalService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.tgBot.service.BotMessageService;
import com.tl.tgGame.util.CompareUtil;
import com.tl.tgGame.wallet.dto.NotifyDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class WalletHandleService {

    @Resource
    private RechargeService rechargeService;
    @Resource
    private WithdrawalService withdrawalService;
    @Resource
    private CurrencyService currencyService;

    @Resource
    private BotMessageService botMessageService;

    @Resource
    private ConfigService configService;


    @Transactional(rollbackFor = Exception.class)
    public void handleNotify(NotifyDTO notifyDTO) {
        switch (notifyDTO.getType()) {
            case RECHARGE:
                rechargeHandle(notifyDTO);
                break;
            case WITHDRAW:
                withdrawHandle(notifyDTO);
                break;
        }
    }


    private void withdrawHandle(NotifyDTO notifyDTO) {
        Currency currency = currencyService.getByUid(notifyDTO.getUid());
        if (currency != null) {
            Withdrawal withdrawal = withdrawalService.lambdaQuery().eq(Withdrawal::getOrderId, notifyDTO.getOrderId()).one();
            if (withdrawal != null) {
                withdrawal.setStatus(notifyDTO.isSuccess() ? WithdrawStatus.withdraw_success : WithdrawStatus.withdraw_fail);
                withdrawal.setHash(notifyDTO.getHash());
                withdrawal.setCompleteTime(notifyDTO.getCompleteTime());
                withdrawalService.updateById(withdrawal);
                if (notifyDTO.isSuccess()) {
                    currencyService.reduce(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "提现成功扣除解冻余额");
                } else {
                    currencyService.unfreeze(withdrawal.getUid(), withdrawal.getUserType(), BusinessEnum.WITHDRAW, withdrawal.getAmount(), withdrawal.getId(), "提现失败解冻");
                }
            }
        }
    }

    private void rechargeHandle(NotifyDTO notifyDTO) {
        Currency currency = currencyService.getByUid(notifyDTO.getUid());
        if (currency != null) {
            // 只处理大于等于100的
            if (CompareUtil.isGreaterThan(notifyDTO.getAmount(), new BigDecimal("100"))) {
                Recharge recharge = Recharge.builder()
                        .id(notifyDTO.getOrderId())
                        .toAddress(notifyDTO.getToAddress())
                        .fromAddress(notifyDTO.getFromAddress())
                        .hash(notifyDTO.getHash())
                        .userType(currency.getUserType())
                        .userId(notifyDTO.getUid())
                        .network(notifyDTO.getNetwork())
                        .screen(null)
                        .amount(notifyDTO.getAmount())
                        .note("链上充值")
                        .createTime(LocalDateTime.now())
                        .build();
                rechargeService.save(recharge);
                currencyService.increase(notifyDTO.getUid(),
                        currency.getUserType(),
                        BusinessEnum.RECHARGE,
                        recharge.getAmount(),
                        recharge.getId(),
                        "充值-" + notifyDTO.getNetwork());

                String beginGameLink = configService.get(ConfigConstants.BOT_BEGIN_GAME_GROUP_LINK);

                List<InlineKeyboardButton> keyboardButtons = Collections.singletonList(InlineKeyboardButton.builder().text("返回游戏大厅").url(beginGameLink).build());
                botMessageService.sendMessage2UserAsync(notifyDTO.getUid(), "您已成功上分" + recharge.getAmount().toPlainString() + "USDT，可以尽情享受游戏",
                        InlineKeyboardMarkup.builder().keyboardRow(keyboardButtons).build()
                );

            } else {
                List<InlineKeyboardButton> keyboardButtons = Collections.singletonList(InlineKeyboardButton.builder().text("唯一充提财务").url("https://t.me/cin89886").build());
                botMessageService.sendMessage2UserAsync(notifyDTO.getUid(), "尊贵的用户，充值金额需大于100USDT，否则充值将无法自动到账！充值地址，单击即可复制，请务必复制或输入正确的充值地址，否则造成的损失平台概不负责！如果您需要人工为您充值，请点击下方“唯一充提财务”按钮，我们将1对1为您提供人工充值服务。感谢您的支持！",
                        InlineKeyboardMarkup.builder().keyboardRow(keyboardButtons).build()
                );
            }

        }
    }
}
