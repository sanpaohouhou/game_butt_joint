package com.tl.tgGame.tgBot.service;


import com.tl.tgGame.project.service.impl.UserServiceImpl;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Component
@Slf4j
public class FakeRechargeMessageTask {
    private final Random random = new Random();
    @Resource
    private ConfigService configService;
    @Resource
    private BotMessageService botMessageService;

    // 每分钟运行一次
    @Scheduled(fixedDelay = 60_000)
    public void doSendMessage() {

        float probability = random.nextFloat(); // 生成0-1之间的随机浮点数

        // 有大约8.33%的概率在这一分钟内发送消息，这样大约在一个小时内会发送5次左右
        if (probability < 0.0833) {
            try {
                log.info("开始发送假充值消息");
                // 发送消息
                String chat = configService.getOrDefault(ConfigConstants.BOT_BEGIN_GAME_GROUP_CHAT, null);
                if (StringUtils.isNotBlank(chat)) {
                    int dummy = (2 + random.nextInt(50)) * 100;
                    botMessageService.sendMessageAsync(chat, "♠️389.bet♠️\n" +
                            "\uD83D\uDCE3贵宾" + UserServiceImpl.convertAccount() + "❤️\n" +
                            "已成功上分：" + dummy + "USDT\n" +
                            "祝您福气满满，财源滚滚‼️", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedDelay = 120_000)
    public void doWithdrawalSendMessage() {

        float probability = random.nextFloat(); // 生成0-1之间的随机浮点数

        // 有大约8.33%的概率在这一分钟内发送消息，这样大约在一个小时内会发送5次左右
        if (probability < 0.0833) {
            try {
                log.info("开始发送假提现消息");
                // 发送消息
                String chat = configService.getOrDefault(ConfigConstants.BOT_BEGIN_GAME_GROUP_CHAT, null);
                if (StringUtils.isNotBlank(chat)) {
                    int dummy = (2 + random.nextInt(50)) * 100;
                    botMessageService.sendMessageAsync(chat, "♠\uFE0F389.bet♠\uFE0F\n" +
                            "\uD83D\uDCE3贵宾\uFE0F\n" + UserServiceImpl.convertAccount() + "❤\n" +
                            "已成功下分: " + dummy + "USDT\n" +
                            "\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
