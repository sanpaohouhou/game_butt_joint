package com.tl.tgGame.tgBot.service.telegram;

import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.system.mapper.Config;
import com.tl.tgGame.tgBot.entity.UserBot;
import com.tl.tgGame.tgBot.service.UserBotRepository;
import com.tl.tgGame.util.NumberUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.games.CallbackGame;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class TelegramBot2 extends TelegramLongPollingBot {

    private String telegram_token;
    private String telegram_username;
    private Long telegram_chat_id;

    public TelegramBot2() {
        this(new DefaultBotOptions());
    }

    public TelegramBot2(DefaultBotOptions options) {
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
    private UserBotRepository userBotRepository;

    @Autowired
    private ConfigService configService;


    private static final List<String> KEYS = Arrays.asList("开始游戏", "个人资料", "游戏报表"
            , "获利查询", "推广链接", "推广数据", "/start", "USDT充值", "USDT提现", "绑定地址");

    public void sendCallBackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        try {
            if (callbackQuery.getData().equals("USDT充值:转账金额确认")) {
                StringBuilder append = new StringBuilder()
                        .append("充值地址: ").append("TE2kinqr93ZeWGLUAvw33JCq4sMVA4oPfJ").append("\r\n")
                        .append("充值分数: ").append("1").append("\r\n")
                        .append("付款金额: ").append("11").append("\r\n")
                        .append("充值有效时长: ").append("11").append("\r\n")
                        .append("尊贵的用户，充值地址与付款金额，单击即可复制，请务必复制! 付款完成后请务必截图，请点击下方“唯一充提财务”按钮，发送财务确认后到账").append("\r\n");
                InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder().url("https://t.me/shanpao_test_bot").text("唯一充提财务").build();
                List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                inlineKeyboardButtons.add(inlineKeyboardButton);
                InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();
                SendMessage message = SendMessage.builder().chatId(callbackQuery.getMessage().getChatId())
                        .text(append.toString()).replyMarkup(inlineKeyboardMarkup).build();
                execute(message);
            }
            if (callbackQuery.getData().equals("USDT提现:确认提现")) {
                // TODO: 2023/8/4  判断余额
                BigDecimal amount = BigDecimal.valueOf(100);
                BigDecimal amount1 = BigDecimal.valueOf(10);
                SendMessage message = null;
                if (amount1.compareTo(BigDecimal.valueOf(20)) < 0) {
                    message = SendMessage.builder().chatId(callbackQuery.getMessage().getChatId().toString())
                            .text("尊敬的用户,可提现金额需大于20USDT").build();
                    execute(message);
                }
                if (amount.compareTo(BigDecimal.valueOf(20)) >= 0) {
                    message = SendMessage.builder().chatId(callbackQuery.getMessage().getChatId().toString())
                            .text("提现待审核,请稍等~~~").build();
                    execute(message);
                }

            }
        } catch (TelegramApiException e) {
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
    }

    public void sendMsg(Update update) {
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();

        KeyboardButton keyboardButton1 = KeyboardButton.builder().text("开始游戏").build();
        KeyboardButton keyboardButton2 = KeyboardButton.builder().text("个人资料").build();
        KeyboardButton keyboardButton3 = KeyboardButton.builder().text("游戏报表").build();

        KeyboardButton keyboardButton4 = KeyboardButton.builder().text("获利查询").build();
        KeyboardButton keyboardButton5 = KeyboardButton.builder().text("USDT充值").build();
        KeyboardButton keyboardButton6 = KeyboardButton.builder().text("USDT提现").build();

        KeyboardButton keyboardButton7 = KeyboardButton.builder().text("绑定地址").build();
        KeyboardButton keyboardButton8 = KeyboardButton.builder().text("推广链接").build();
        KeyboardButton keyboardButton9 = KeyboardButton.builder().text("推广数据").build();


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

            if (!update.hasMessage()) {
                ErrorEnum.SYSTEM_ERROR.throwException();
            }
            String text = update.getMessage().getText();
            if (KEYS.contains(text)) {
                buildState(update, false);
            }
            if (text.equals("开始游戏")) {
                InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().url("https://t.me/shanpao_test_bot").text("\uD83D\uDC9E游戏大厅").build();
                List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                inlineKeyboardButtons.add(inlineKeyboardButton1);
                InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();

                SendMessage sendMessage = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("请点击下方按钮开始您的游戏之旅").replyMarkup(inlineKeyboardMarkup).build();
                execute(sendMessage);
            }
            if (text.equals("个人资料")) {
                StringBuilder builder = new StringBuilder();
                StringBuilder append = builder.append("游戏账号: ").append("username").append("\r\n")
                        .append("提现地址: ").append("withdrawal").append("\r\n")
                        .append("充值金额: ").append("withdrawalAmount").append("\r\n")
                        .append("总充值: ").append("allRecharge").append("\r\n")
                        .append("总提现: ").append("allWithdrawal").append("\r\n")
                        .append("总返水: ").append("allBackWater").append("\r\n")
                        .append("待返水: ").append("waitBackWater").append("\r\n")
                        .append("总佣金: ").append("allCommission").append("\r\n")
                        .append("总彩金: ").append("allProfit").append("\r\n")
                        .append("提现待审核: ").append("waitAuthWithdrawal").append("\r\n")
                        .append("\r\n")
                        .append(" 用户返水及推广佣金会因不同游戏设置不同比例，详情请点击“获利查询”查看详况 ").append("\r\n");
                SendMessage message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text(append.toString()).build();
                execute(message);
            }
            if (text.equals("游戏报表")) {
                StringBuilder append1 = new StringBuilder()
                        .append("-------当日报表--------").append("\r\n")
                        .append("总下注: ").append("0").append("\r\n")
                        .append("总派彩: ").append("0").append("\r\n")
                        .append("总盈利: ").append("0").append("\r\n")
                        .append("\r\n")

                        .append("-------当周报表--------").append("\r\n")
                        .append("总下注: ").append("0").append("\r\n")
                        .append("总派彩: ").append("0").append("\r\n")
                        .append("总盈利: ").append("0").append("\r\n")
                        .append("\r\n")

                        .append("-------当月报表--------").append("\r\n")
                        .append("总下注: ").append("0").append("\r\n")
                        .append("总派彩: ").append("0").append("\r\n")
                        .append("总盈利: ").append("0").append("\r\n");

                SendMessage message1 = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text(append1.toString()).build();
                execute(message1);
            }
            if (text.equals("获利查询")) {
                List<KeyboardRow> list1 = new ArrayList<>();
                KeyboardRow keyboardRow10 = new KeyboardRow();
                KeyboardButton keyboardButton10 = KeyboardButton.builder().text("\uD83D\uDC9EFC电子").build();
                KeyboardButton keyboardButton11 = KeyboardButton.builder().text("\uD83D\uDD3A返回上级\uD83D\uDD19").build();

                keyboardRow10.add(keyboardButton10);
                keyboardRow10.add(keyboardButton11);
                list1.add(keyboardRow10);
                ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder().keyboard(list1).resizeKeyboard(true).build();

                SendMessage message2 = SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text("尊贵的用户，请点击下列游戏查看 对应的返水、推广佣金比例及数据")
                        .replyMarkup(replyKeyboardMarkup)
                        .build();
                execute(message2);
            }
            if (text.equals("\uD83D\uDC44FC电子")) {
                StringBuilder append2 = new StringBuilder()
                        .append("\uD83D\uDC44FC电子")
                        .append("游戏名称: ").append("\uD83D\uDC44FC电子").append("\r\n")
                        .append("返水比例: ").append("1").append("\r\n")
                        .append("已返水: ").append("1").append("\r\n")
                        .append("待返水: ").append("1").append("\r\n")
                        .append("下级佣金比例: ").append("1").append("\r\n")
                        .append("下级佣金: ").append("1").append("\r\n");
                SendMessage message4 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text(append2.toString()).build();
                execute(message4);
            }
            if (text.equals("\uD83D\uDD3A返回上级\uD83D\uDD19")) {
                SendMessage message5 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text("\uD83C\uDF89欢迎您来到389.bet博彩娱乐综合城‼\uFE0F \uD83C\uDF1F无需注册，点击开始游戏，即可闪电加入⚡\uFE0F")
                        .replyMarkup(keyboardMarkup).build();
                execute(message5);
            }

            if (text.equals("USDT充值") || checkState(update).equals("USDT充值")) {
                SendMessage message8 = null;
                if (update.getMessage().getText().equals("USDT充值")) {
                    buildState(update, true);
                    message8 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，请输入充值金额").build();
                } else {
                    List<List<InlineKeyboardButton>> lists = new ArrayList<>();

                    List<InlineKeyboardButton> inlineKeyboardButtons1 = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton2 = InlineKeyboardButton.builder().url("https://t.me/shanpao_test_bot").text("唯一充提财务").build();
                    inlineKeyboardButtons1.add(inlineKeyboardButton2);
                    InlineKeyboardMarkup inlineKeyboardMarkup1 = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons1).build();
                    if (!NumberUtil.isParsable(update.getMessage().getText()) || !NumberUtil.isNumeric2(update.getMessage().getText())) {
                        message8 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("尊贵的用户，请正确输入金额").replyMarkup(inlineKeyboardMarkup1).build();
                    } else if (Integer.parseInt(update.getMessage().getText()) < 100) {
                        message8 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("尊贵的用户，最低充值：100USDT").replyMarkup(inlineKeyboardMarkup1).build();
                    } else {
                        StringBuilder append3 = new StringBuilder()
                                .append("名称: ").append("11").append("\r\n")
                                .append("充值分数: ").append("1").append("\r\n")
                                .append("付款金额: ").append("1").append("\r\n")
                                .append("尊贵的用户，请确认转账金额，如果无法正确转入系统指定付款金额，将无法完成游戏分数的充值。" +
                                        "劳您再次确认，确认无误后请点击下方“转账金额确认”按钮。").append("\r\n");
                        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

                        InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder()
                                .text("转账金额确认").callbackData("USDT充值:转账金额确认").build();
                        inlineKeyboardButtons.add(inlineKeyboardButton);
                        lists.add(inlineKeyboardButtons1);
                        lists.add(inlineKeyboardButtons);

                        InlineKeyboardMarkup inlineKeyboardMarkup2 = InlineKeyboardMarkup.builder().keyboard(lists).build();
                        message8 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text(append3.toString()).replyMarkup(inlineKeyboardMarkup2).build();
                    }
                }
                execute(message8);
            }

            if (text.equals("USDT提现")) {
                // TODO: 2023/8/4 查询一下用户有没有绑定过
                List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder().url("https://t.me/shanpao_test_bot").text("唯一充提财务").build();
                inlineKeyboardButtons.add(inlineKeyboardButton);
                SendMessage message = null;
                if (!true) {
                    InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊敬的用户，请绑定充值提现地址").replyMarkup(inlineKeyboardMarkup).build();
                } else {
                    List<List<InlineKeyboardButton>> lists = new ArrayList<>();
                    InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().text("确认提现").callbackData("USDT提现:确认提现").build();
                    List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();

                    inlineKeyboardButtonList.add(inlineKeyboardButton1);
                    lists.add(inlineKeyboardButtons);
                    lists.add(inlineKeyboardButtonList);

                    InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboard(lists).build();
                    StringBuilder append = new StringBuilder()
                            .append("可提现金额: ").append("0").append("\r\n");
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text(append.toString()).replyMarkup(inlineKeyboardMarkup).build();
                }
                execute(message);
            }

            if (text.equals("绑定地址")) {
                // TODO: 2023/8/4 查询用户是否绑定地址
                SendMessage message = null;
                if (!false) {
                    buildState(update, true);
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，请输入TRC-20地址进行提现绑定").build();
                } else {
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，已绑定您的TRC-20地址").build();
                }
                execute(message);
            } else if (checkState(update).equals("绑定地址")) {
                SendMessage message = null;
                if (!text.startsWith("TRC_20")) {
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，请输入正确的TRC-20地址。").build();
                } else {
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，已绑定您的TRC-20地址").build();
                }
                execute(message);
            }
            if (text.equals("推广数据")) {
                StringBuilder append3 = new StringBuilder()
                        .append("-------下级用户--------").append("\r\n")
                        .append("人数: ").append("111").append("\r\n")
                        .append("当日转码: ").append("1").append("\r\n")
                        .append("当月转码: ").append("1").append("\r\n")
                        .append("已结佣金: ").append("1").append("\r\n")
                        .append("当日总存款: ").append("1").append("\r\n")
                        .append("当月总存款: ").append("1").append("\r\n")
                        .append("当日总提款: ").append("1").append("\r\n")
                        .append("当月总提款: ").append("1").append("\r\n")
                        .append("当日总返水: ").append("1").append("\r\n")
                        .append("当日总彩金: ").append("1").append("\r\n")
                        .append("当月总彩金: ").append("1").append("\r\n");
                SendMessage message7 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text(append3.toString()).build();
                execute(message7);
            }
            if (text.equals("推广链接")) {
                SendMessage message6 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text("全民推广，人人获利！\n" +
                                "\n" +
                                "邀请好友入群或将您的专属推广链接发送好友，好友点击入群即成为您的下级推广，下级投注均会自动返佣至您钱包余额！\n" +
                                "\n" +
                                "不同游戏对应不同返佣系数，详情请点击“下级数据”和“获利查询”查看\n" +
                                "\n" +
                                "尊贵的用户，您的推广链接为：" + "等有域名这里在正式改").build();
                execute(message6);
            }
            if (text.equals("/start")) {
                SendMessage message3 = SendMessage.builder().text("\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25")
                        .replyMarkup(keyboardMarkup).chatId(update.getMessage().getChatId().toString())
                        .build();
                execute(message3);
            }
        } catch (
                TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }


    private String checkState(Update update) {
        Optional<UserBot> botOptional = userBotRepository.findById(update.getMessage().getFrom().getId());
        if (!botOptional.isPresent()) {
            return update.getMessage().getText();
        }
        UserBot userBot = botOptional.get();
        if (userBot.getState()) {
            return userBot.getText();
        }
        return update.getMessage().getText();
    }

    private UserBot buildState(Update update, Boolean state) {
        Message message = update.getMessage();
        UserBot userBot = UserBot.builder()
                .state(state)
                .text(message.getText())
                .botUserId(message.getFrom().getId())
                .chatId(message.getChatId()).build();
        userBotRepository.save(userBot);
        return userBot;
    }


    public static void main(String[] args) {
        boolean sjdj1 = NumberUtils.isParsable("1");
        System.out.println(sjdj1);
    }
}

