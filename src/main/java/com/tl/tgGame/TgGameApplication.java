package com.tl.tgGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TgGameApplication {

	public static void main(String[] args) {
//		try {
//			TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//			MyBot myBot = new MyBot();
//			telegramBotsApi.registerBot(myBot);
//		} catch (TelegramApiException e) {
//			throw new RuntimeException(e);
//		}
		SpringApplication.run(TgGameApplication.class, args);
	}

}
