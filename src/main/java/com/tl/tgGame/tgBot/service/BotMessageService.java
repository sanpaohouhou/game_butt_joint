package com.tl.tgGame.tgBot.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import kong.unirest.HttpResponse;
import kong.unirest.MultipartBody;
import kong.unirest.UnirestInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class BotMessageService {
    @Resource
    private UnirestInstance unirestInstance;
    @Resource
    private ConfigService configService;
    @Resource
    private UserService userService;
    @Resource
    private Executor taskExecutor;

    public void sendMessage(String chat, String text, ReplyKeyboard replyKeyboard){
        String token = configService.getOrDefault(ConfigConstants.TG_BOT_TOKEN_TWO, "6537817937:AAEr4vSYuWrrLGcBKlLGAuPm7WoYqb6iZ1A");
        HttpResponse<String> stringHttpResponse = null;
        try {
            MultipartBody multipartBody = unirestInstance.post("https://api.telegram.org/bot" + token + "/sendMessage")
                    .contentType("application/x-www-form-urlencoded")
                    .field("chat_id", chat).field("text", text);
            if (replyKeyboard != null) {
                multipartBody.field("reply_markup", new ObjectMapper().writeValueAsString(replyKeyboard));
            }
            stringHttpResponse = multipartBody.asString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("bot发送消息: {}", stringHttpResponse.getBody());
    }

    public void sendMessageAsync(String chat, String text, ReplyKeyboard replyKeyboard){
        taskExecutor.execute(() -> {
            sendMessage(chat, text, replyKeyboard);
        });
    }

    public void sendMessage2User(Long uid, String text, ReplyKeyboard replyKeyboard){
        User u = userService.getById(uid);
        if (u != null){
            sendMessage(String.valueOf(u.getTgId()), text, replyKeyboard);
        }
    }

    public void sendMessage2UserAsync(Long uid, String text, ReplyKeyboard replyKeyboard){
        User u = userService.getById(uid);
        if (u != null){
            sendMessageAsync(String.valueOf(u.getTgId()), text, replyKeyboard);
        }
    }
}
