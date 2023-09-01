package com.tl.tgGame.tgBot.service.telegram;

import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.project.dto.ApiEgCreateUserReq;
import com.tl.tgGame.project.dto.ApiLoginReq;
import com.tl.tgGame.project.dto.ApiSetPointReq;
import com.tl.tgGame.project.dto.ApiSetPointRes;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendGame;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private String telegram_token;
    private String telegram_username;
    private Long telegram_chat_id;

    public TelegramBot() {
        this(new DefaultBotOptions());
    }

    public TelegramBot(DefaultBotOptions options) {
        super(options);
    }

    public String getTelegram_token() {
        return telegram_token;
    }

    public void setTelegram_token(String telegram_token) {
        this.telegram_token = telegram_token;
    }

    public String getTelegram_username() {
        return telegram_username;
    }

    public void setTelegram_username(String telegram_username) {
        this.telegram_username = telegram_username;
    }

    public Long getTelegram_chat_id() {
        return telegram_chat_id;
    }

    public void setTelegram_chat_id(Long telegram_chat_id) {
        this.telegram_chat_id = telegram_chat_id;
    }

    @Override
    public String getBotToken() {
        return this.telegram_token;
    }

    @Override
    public String getBotUsername() {
        return this.telegram_username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            sendCallBackQuery(update);
        }
        if (update.hasMessage()) {
            sendMsg(update);
        }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthTokenService authTokenService;

    public void sendCallBackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        try {
            User from = callbackQuery.getFrom();
            if (callbackQuery.getGameShortName() != null && callbackQuery.getGameShortName().equals("FC_GAME")) {
                com.tl.tgGame.project.entity.User user = userService.checkTgId(from.getId());
                String login = gameService.login(ApiLoginReq.builder().MemberAccount(user.getGameAccount()).LoginGameHall(true).LanguageID(2).build());
                Boolean result = userService.gameRecharge(user.getTgId(), GameBusiness.FC.getKey());
                log.info("Fc发财电子游戏登录链接:{},rechargeResult:{}", login, result);
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl(login);
                answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
                execute(answerCallbackQuery);
            }
            if (callbackQuery.getGameShortName() != null && callbackQuery.getGameShortName().equals("EG_GAME")) {
                com.tl.tgGame.project.entity.User user = userService.checkTgId(from.getId());
                String token = authTokenService.login(user.getId());
                if (!user.getHasJoinEg()) {
                    String merch = configService.get(ConfigConstants.EG_AGENT_CODE);
                    Boolean createUser = gameService.egCreateUser(ApiEgCreateUserReq.builder().isBot(user.getIsBot()).playerId(user.getId().toString()).merch(merch)
                            .currency("USDT").build());
                    if (createUser) {
                        user.setHasJoinEg(true);
                        userService.updateById(user);
                    }
                }
                Boolean result = userService.gameRecharge(user.getTgId(), GameBusiness.EG.getKey());
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl("http://192.168.28.113:5173/#/home?token=" + token);
                answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
                execute(answerCallbackQuery);
            }
            if(callbackQuery.getGameShortName() != null && callbackQuery.getGameShortName().equals("WL_GAME")){
                com.tl.tgGame.project.entity.User user = userService.checkTgId(from.getId());
                Boolean result = userService.gameRecharge(user.getTgId(), GameBusiness.WL.getKey());
                String wlEnterGame = gameService.wlEnterGame(user.getId(), null,request);
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl(wlEnterGame);
                answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
                execute(answerCallbackQuery);
            }
            if(callbackQuery.getGameShortName() != null && callbackQuery.getGameShortName().equals("WL_BJL")){
                com.tl.tgGame.project.entity.User user = userService.checkTgId(from.getId());
                Boolean result = userService.gameRecharge(user.getTgId(), GameBusiness.WL.getKey());
                String wlEnterGame = gameService.wlEnterGame(user.getId(), "81",request);
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl(wlEnterGame);
                answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
                execute(answerCallbackQuery);
            }
            if(callbackQuery.getGameShortName() != null && callbackQuery.getGameShortName().equals("WL_TY")){
                com.tl.tgGame.project.entity.User user = userService.checkTgId(from.getId());
                Boolean result = userService.gameRecharge(user.getTgId(), GameBusiness.WL.getKey());
                String wlEnterGame = gameService.wlEnterGame(user.getId(), "100",request);
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setUrl(wlEnterGame);
                answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
                execute(answerCallbackQuery);
            }
        } catch (Exception e) {
            log.error("电子开始游戏异常exception:{},callBackQuery:{}", e,callbackQuery.getGameShortName() + callbackQuery.getFrom().getId());
        }
    }

    public void sendMsg(Update update) {
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardRow keyboardRow1 = new KeyboardRow();

        KeyboardRow keyboardRow2 = new KeyboardRow();

        KeyboardButton keyboardButton1 = KeyboardButton.builder().text("\uD83D\uDC9EFC电子").build();
        KeyboardButton keyboardButton2 = KeyboardButton.builder().text("\uD83C\uDFB0WL棋牌").build();
        KeyboardButton keyboardButton3 = KeyboardButton.builder().text("\uD83D\uDC21EG电子").build();

        KeyboardButton keyboardButton4 = KeyboardButton.builder().text("\uD83D\uDC9EWL百家乐").build();
        KeyboardButton keyboardButton5 = KeyboardButton.builder().text("⚽\uFE0FWL体育").build();
        KeyboardButton keyboardButton6 = KeyboardButton.builder().text("\uD83E\uDD29个人中心\uD83C\uDF08").build();

        KeyboardButton keyboardButton7 = KeyboardButton.builder().text("\uD83E\uDD29提现\uD83C\uDF08").build();
        KeyboardButton keyboardButton8 = KeyboardButton.builder().text("\uD83E\uDD29推广\uD83C\uDF08").build();
        KeyboardButton keyboardButton9 = KeyboardButton.builder().text("\uD83D\uDC96专属客服\uD83D\uDE47\u200D♀\uFE0F").build();

        keyboardRow.add(keyboardButton1);
        keyboardRow.add(keyboardButton2);
        keyboardRow.add(keyboardButton3);

        keyboardRow1.add(keyboardButton4);
        keyboardRow1.add(keyboardButton5);
        keyboardRow1.add(keyboardButton6);

        keyboardRow2.add(keyboardButton7);
        keyboardRow2.add(keyboardButton8);
        keyboardRow2.add(keyboardButton9);

        list.add(keyboardRow);
        list.add(keyboardRow1);
        list.add(keyboardRow2);

        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder().keyboard(list)
                .resizeKeyboard(true).build();
        try {
            if (update.getMessage() != null) {
                Message message2 = update.getMessage();
                Chat chat = message2.getChat();
                if (!CollectionUtils.isEmpty(message2.getNewChatMembers())) {
                    List<User> newChatMembers = message2.getNewChatMembers();
                    for (User users : newChatMembers) {
                        com.tl.tgGame.project.entity.User user = userService.checkTgIdAndGroup(users.getId(), chat.getId().toString());
                        if (user == null) {
                            userService.insertUser(users.getFirstName(), users.getLastName(), users.getUserName(),
                                    users.getIsBot(), users.getId(), chat.getId().toString());
                        } else {
                            userService.updateByHasGroup(users.getId(), chat.getId().toString(), true);
                        }
                    }
                }
                if (message2.getLeftChatMember() != null) {
                    Chat chats = update.getMessage().getChat();
                    userService.updateByHasGroup(message2.getLeftChatMember().getId(), chats.getId().toString(), false);
                    return;
                }
                if (message2.getText() != null) {
                    InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().callbackGame(new CallbackGame()).text("\uD83C\uDFC6开始游戏\uD83D\uDD25").build();
                    InlineKeyboardButton inlineKeyboardButton2 = InlineKeyboardButton.builder().url("https://t.me/first_new_one_bot").text("\uD83E\uDD29个人中心\uD83C\uDF08").build();
                    List<List<InlineKeyboardButton>> lists = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                    List<InlineKeyboardButton> inlineKeyboardButtonsS = new ArrayList<>();
                    inlineKeyboardButtons.add(inlineKeyboardButton1);

                    inlineKeyboardButtonsS.add(inlineKeyboardButton2);

                    lists.add(inlineKeyboardButtons);
                    lists.add(inlineKeyboardButtonsS);
                    InlineKeyboardMarkup build = InlineKeyboardMarkup.builder().keyboard(lists).build();

                    InlineKeyboardButton inlineKeyboardButton3 = InlineKeyboardButton.builder().url("https://t.me/first_new_one_bot").text("\uD83E\uDD29个人中心\uD83C\uDF08").build();
                    List<InlineKeyboardButton> inlineKeyboardButtons3 = new ArrayList<>();
                    inlineKeyboardButtons3.add(inlineKeyboardButton3);
                    InlineKeyboardMarkup inlineKeyboardMarkup3 = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons3).build();

                    switch (message2.getText()) {
                        case "\uD83D\uDC9EFC电子":
                            SendGame build1 = SendGame.builder().chatId(update.getMessage().getChatId())
                                    .gameShortName("FC_GAME")
                                    .replyMarkup(build)
                                    .build();
                            execute(build1);
                            break;
                        case "\uD83C\uDFB0WL棋牌":
                            SendGame build2 = SendGame.builder().chatId(update.getMessage().getChatId())
                                    .gameShortName("WL_GAME")
                                    .allowSendingWithoutReply(false)
                                    .replyMarkup(build)
                                    .build();
                            execute(build2);
                            break;
                        case "\uD83D\uDC21EG电子":
                            SendGame build3 = SendGame.builder().chatId(update.getMessage().getChatId())
                                    .gameShortName("EG_GAME")
                                    .allowSendingWithoutReply(false)
                                    .replyMarkup(build)
                                    .build();
                            execute(build3);
                            break;
                        case "\uD83D\uDC9EWL百家乐":
                            SendGame build4 = SendGame.builder().chatId(update.getMessage().getChatId())
                                    .gameShortName("WL_BJL")
                                    .allowSendingWithoutReply(false)
                                    .replyMarkup(build)
                                    .build();
                            execute(build4);
                            break;
                        case"⚽\uFE0FWL体育":
                            SendGame build5 = SendGame.builder().chatId(update.getMessage().getChatId())
                                    .gameShortName("WL_TY")
                                    .allowSendingWithoutReply(false)
                                    .replyMarkup(build)
                                    .build();
                            execute(build5);
                            break;
                        case "\uD83E\uDD29提现\uD83C\uDF08":
                            SendMessage message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                    .text("\uD83D\uDC9E \uD83D\uDC9E \uD83D\uDC9E您好，请进入个中心进行提现！！！")
                                    .replyMarkup(inlineKeyboardMarkup3).build();
                            execute(message);
                            break;
                        case "\uD83E\uDD29推广\uD83C\uDF08":
                            SendMessage message1 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                    .text("\uD83D\uDC9E \uD83D\uDC9E \uD83D\uDC9E您好，请进入个中心查看推广规则！！！")
                                    .replyMarkup(inlineKeyboardMarkup3).build();
                            execute(message1);
                            break;
                        case "\uD83E\uDD29个人中心\uD83C\uDF08":
                            SendMessage message3 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                    .text("\uD83D\uDC9E \uD83D\uDC9E \uD83D\uDC9E您好，请进入个人中心查询信息！！！")
                                    .replyMarkup(inlineKeyboardMarkup3).build();
                            execute(message3);
                            break;
                        case "\uD83D\uDC96唯一专属客服\uD83D\uDE47\u200D♀\uFE0F":
                            InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder().url("https://t.me/cin89886").text("\uD83D\uDC96唯一专属客服\uD83D\uDE47\u200D♀\uFE0F").build();
                            List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
                            inlineKeyboardButtons1.add(inlineKeyboardButton);
                            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons1).build();
                            SendMessage message4 = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("\uD83C\uDFC6389.bet 综合娱乐城为您提供全网唯一综合性博彩游戏投注平台\n" +
                                            "\n" +
                                            "\uD83C\uDFC6无需注册、一键登录、零级门槛\n" +
                                            "\uD83C\uDFC6极速投注、一键返水、秒级到账\n" +
                                            "\uD83C\uDFC6全民代理、高额回佣、无忧托管\n" +
                                            "\n" +
                                            "\uD83D\uDCB8USDT充值提现，大额无忧\n" +
                                            "\uD83D\uDCB8多币种三方结算，车队服务\n" +
                                            "\n" +
                                            "\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25\n" +
                                            "\n" +
                                            "尊贵的用户，进入个人中心充值USDT即可畅玩各大厂商头牌游戏，如有任何疑问请咨询本群唯一专属客服。\n" +
                                            "\uD83D\uDC81\u200D♀\uFE0F本平台客服、财务严禁私自联系用户，任何私聊您均是骗子‼\uFE0F")
                                    .parseMode("html").replyMarkup(inlineKeyboardMarkup).build();
                            execute(message4);
                            break;
                        default:
                            SendMessage message5 = SendMessage.builder().text("\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25")
                                    .replyMarkup(keyboardMarkup).chatId(update.getMessage().getChatId().toString())
                                    .build();
                            execute(message5);
                            break;
                    }
                } else {
                    SendMessage message3 = SendMessage.builder().text("\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25")
                            .replyMarkup(keyboardMarkup).chatId(update.getMessage().getChatId().toString())
                            .build();
                    execute(message3);
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

