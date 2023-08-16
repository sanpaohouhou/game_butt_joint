package com.tl.tgGame.tgBot.service.telegram;

import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

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
        sendMsg(update);
    }

    public void sendMsg(Update update) {
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        KeyboardRow keyboardRow1 = new KeyboardRow();

        KeyboardButton keyboardButton1 = KeyboardButton.builder().text("\uD83D\uDC9EFC电子").build();

        KeyboardButton keyboardButton2 = KeyboardButton.builder().text("\uD83E\uDD29个人中心\uD83C\uDF08").build();
        KeyboardButton keyboardButton3 = KeyboardButton.builder().text("\uD83D\uDC96唯一专属客服\uD83D\uDE47\u200D♀\uFE0F").build();

        keyboardRow.add(keyboardButton1);
        keyboardRow1.add(keyboardButton2);
        keyboardRow1.add(keyboardButton3);

        list.add(keyboardRow);
        list.add(keyboardRow1);
        ReplyKeyboardMarkup keyboardMarkup = ReplyKeyboardMarkup.builder().keyboard(list)
                .resizeKeyboard(true).build();
        try {

            if (update.getMessage() != null) {
                switch (update.getMessage().getText()) {
                    case "\uD83D\uDC9EFC电子":
                        InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().url("https://369.bet").text("\uD83C\uDFC6开始游戏\uD83D\uDD25").build();
                        InlineKeyboardButton inlineKeyboardButton2 = InlineKeyboardButton.builder().url("https://t.me/wangwang_xin_bot").text("\uD83E\uDD29个人中心\uD83C\uDF08").build();
                        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardButtonsS = new ArrayList<>();
                        inlineKeyboardButtons.add(inlineKeyboardButton1);

                        inlineKeyboardButtonsS.add(inlineKeyboardButton2);

                        lists.add(inlineKeyboardButtons);
                        lists.add(inlineKeyboardButtonsS);
                        InlineKeyboardMarkup build = InlineKeyboardMarkup.builder().keyboard(lists).build();

                    SendPhoto sendPhoto = SendPhoto.builder().chatId(update.getMessage().getChatId().toString()).caption("389.bet综合娱乐城为您献上极致沉浸的顶级娱乐游戏体验～")
                            .photo(new InputFile("https://tg-game-jp.s3-accelerate.amazonaws.com/file/b33723e1-61fd-40d7-9dd1-409c546a2a67.png"))
                            .parseMode("html")
                            .replyMarkup(build).build();
                    execute(sendPhoto);
                        break;
                    case "\uD83E\uDD29个人中心\uD83C\uDF08":
                        InlineKeyboardButton inlineKeyboardButton3 = InlineKeyboardButton.builder().url("https://t.me/wangwang_xin_bot").text("\uD83E\uDD29个人中心\uD83C\uDF08").build();
                        List<InlineKeyboardButton> inlineKeyboardButtons3 = new ArrayList<>();
                        inlineKeyboardButtons3.add(inlineKeyboardButton3);
                        InlineKeyboardMarkup inlineKeyboardMarkup3 = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons3).build();
                        SendMessage message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("\uD83D\uDC9E \uD83D\uDC9E \uD83D\uDC9E您好，请进入个人中心查询信息！！！")
                                .replyMarkup(inlineKeyboardMarkup3).build();
                        execute(message);
                        break;
                    case "\uD83D\uDC96唯一专属客服\uD83D\uDE47\u200D♀\uFE0F":
                        InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder().url("https://369.bet").text("\uD83D\uDC96唯一专属客服\uD83D\uDE47\u200D♀\uFE0F").build();
                        List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
                        inlineKeyboardButtons1.add(inlineKeyboardButton);
                        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons1).build();
                        SendMessage message1 = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("\uD83C\uDFC6389.bet 综合娱乐城为您提供全网唯一综合性博彩游戏投注平台\n" +
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
                        execute(message1);
                        break;
                    default:
                        SendMessage message3 = SendMessage.builder().text("\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25")
                                .replyMarkup(keyboardMarkup).chatId(update.getMessage().getChatId().toString())
                                .build();
                        execute(message3);
                        break;
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

