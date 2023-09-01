package com.tl.tgGame.tgBot.service.telegram;

import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfig {

    @Autowired
    private ConfigService configService;

    @Bean
    public TelegramBot telegramBot() {
        String telegram_token;
        String telegram_username;
        try {
            String tele_config = configService._get(ConfigConstants.TG_BOT_TOKEN);
            if (StringUtils.isEmpty(tele_config)){
                return new TelegramBot();
            }
            telegram_token = configService.getOrDefault(ConfigConstants.TG_BOT_TOKEN, "6537817937:AAEr4vSYuWrrLGcBKlLGAuPm7WoYqb6iZ1A");
            telegram_username = configService.getOrDefault(ConfigConstants.TG_BOT_NAME, "shanpao_test_bot");
//            telegram_token = "6646772888:AAEeHK75cYImtPlTmVRE5ibCQ1clXwm7DW8";
//            telegram_username = "fc_test_game";
        } catch (Exception e) {
            telegram_token = "6537817937:AAEr4vSYuWrrLGcBKlLGAuPm7WoYqb6iZ1A";
            telegram_username = "shanpao_test_bot";
        }

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot bot2 = new TelegramBot();
            bot2.setTelegram_token(telegram_token);
            bot2.setTelegram_username(telegram_username);
            telegramBotsApi.registerBot(bot2);

            return bot2;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public TelegramBot2 telegramBot2() {
        String telegram_token;
        String telegram_username;
        try {
            String tele_config = configService._get(ConfigConstants.TG_BOT_TOKEN_TWO);
            if (StringUtils.isEmpty(tele_config)){
                return new TelegramBot2();
            }
            telegram_token = configService.getOrDefault(ConfigConstants.TG_BOT_TOKEN_TWO, "6606272964:AAE8eCJgubl8c_y2WNIKNlDYUMFgOcgrzfE");
            telegram_username = configService.getOrDefault(ConfigConstants.TG_BOT_NAME_TWO, "wangwang_xin_bot");
        } catch (Exception e) {
            telegram_token = "6606272964:AAE8eCJgubl8c_y2WNIKNlDYUMFgOcgrzfE";
            telegram_username = "wangwang_xin_bot";
        }

        try {

            TelegramBotsApi telegramBotsApi1 = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot2 telegramBot2 = new TelegramBot2();
            telegramBot2.setTelegram_token(telegram_token);
            telegramBot2.setTelegram_username(telegram_username);
            telegramBotsApi1.registerBot(telegramBot2);

            return telegramBot2;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }




}
