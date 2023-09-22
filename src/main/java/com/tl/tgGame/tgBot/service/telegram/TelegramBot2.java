package com.tl.tgGame.tgBot.service.telegram;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.tl.tgGame.address.AddressService;
import com.tl.tgGame.address.entity.Address;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.dto.BotExtendStatisticsInfo;
import com.tl.tgGame.project.dto.BotGameStatisticsInfo;
import com.tl.tgGame.project.dto.BotPersonInfo;
import com.tl.tgGame.project.dto.GameBusinessStatisticsInfo;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.entity.CurrencyGameProfit;
import com.tl.tgGame.project.enums.BusinessEnum;
import com.tl.tgGame.project.enums.GameBusiness;
import com.tl.tgGame.project.enums.Network;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.tgBot.entity.UserBot;
import com.tl.tgGame.tgBot.service.UserBotRepository;
import com.tl.tgGame.util.NumberUtil;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.wallet.WalletAPI;
import com.tl.tgGame.wallet.dto.RechargeCheckDTO;
import com.tl.tgGame.wallet.dto.SingleResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
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

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private WalletAPI walletAPI;

    @Autowired
    private CurrencyGameProfitService currencyGameProfitService;

    private static final List<String> KEYS = Arrays.asList("开始游戏", "个人资料", "游戏报表"
            , "获利查询", "推广链接", "推广数据", "/start", "USDT充值", "USDT提现", "绑定地址");

    public void sendCallBackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        try {
            // 用户点击转账完成
            if (callbackQuery.getData().equalsIgnoreCase("RECHARGE_CHECK")) {
                com.tl.tgGame.project.entity.User user = userService.checkTgId(callbackQuery.getMessage().getChatId());

                RechargeCheckDTO rechargeCheckDTO = new RechargeCheckDTO();
                rechargeCheckDTO.setChainType("TRON");
                rechargeCheckDTO.setUid(user.getId());

                SingleResponse<Boolean> rechargeCheck = walletAPI.rechargeCheck(rechargeCheckDTO);
                if (!rechargeCheck.getData()) {
                    String callbackId = update.getCallbackQuery().getId();
                    AnswerCallbackQuery answer = new AnswerCallbackQuery();
                    answer.setCallbackQueryId(callbackId);
                    answer.setText("尊贵的用户，充值金额需大于100USDT，否则充值将无法自动到账！充值地址，单击即可复制，请务必复制或输入正确的充值地址，否则造成的损失平台概不负责！如果您需要人工为您充值，请点击下方“唯一充提财务”按钮，我们将1对1为您提供人工充值服务。感谢您的支持！");
                    answer.setShowAlert(true); // 如果你想显示一个弹窗，则设为 true
                    execute(answer);
                } else {
                    execute(DeleteMessage.builder()
                            .chatId(callbackQuery.getMessage().getChatId())
                            .messageId(callbackQuery.getMessage().getMessageId())
                            .build());
                    SendMessage message = SendMessage.builder()
                            .chatId(callbackQuery.getMessage().getChatId())
                            .text("尊贵的用户，正在等待网络确认，支付成功后系统将会在1-2分钟内确认，确认成功之后会通知您！")
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboardRow(
                                            Collections.singletonList(InlineKeyboardButton.builder()
                                                    .url("https://t.me/cin89886").text("唯一充提财务").build()))
                                    .build())
                            .build();
                    execute(message);
                }

            }
            if (callbackQuery.getData().contains("USDT提现:确认提现")) {
                com.tl.tgGame.project.entity.User user = userService.checkTgId(callbackQuery.getMessage().getChatId());
                String[] split = callbackQuery.getData().split("\\|");
                String text = split[1];
                BigDecimal withdrawalAmount = new BigDecimal(text);
                Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
                if (withdrawalAmount.compareTo(currency.getRemain()) > 0) {
                    return;
                }
                execute(DeleteMessage.builder()
                        .chatId(callbackQuery.getMessage().getChatId())
                        .messageId(callbackQuery.getMessage().getMessageId())
                        .build());
                withdrawalService.withdraw(user.getId(), UserType.USER, Network.TRC20, user.getWithdrawalUrl(), withdrawalAmount);
                SendMessage message = SendMessage.builder().chatId(callbackQuery.getMessage().getChatId().toString())
                        .text("提现待审核,请稍等~~~").build();
                execute(message);
            }
            if (callbackQuery.getData().equals("个人资料:一键返水")) {
                com.tl.tgGame.project.entity.User user = userService.checkTgId(callbackQuery.getMessage().getChatId());
                List<CurrencyGameProfit> currencyGameProfits = currencyGameProfitService.queryByUserId(user.getId());
                if (CollectionUtils.isEmpty(currencyGameProfits)) {
                    AnswerCallbackQuery answer = new AnswerCallbackQuery();
                    answer.setCallbackQueryId(callbackQuery.getId());
                    answer.setText("返水失败,请重新操作..待返水: 0");
                    answer.setShowAlert(true);
                    execute(answer);
                }
                BigDecimal allBackWater = BigDecimal.ZERO;
                for (CurrencyGameProfit currencyGameProfit : currencyGameProfits) {
                    allBackWater = allBackWater.add(currencyGameProfit.getBalance());
                    currencyGameProfitService.withdrawal(currencyGameProfit.getUserId(),currencyGameProfit.getGameBusiness(),currencyGameProfit.getBalance());
                }
                AnswerCallbackQuery answer = new AnswerCallbackQuery();
                answer.setCallbackQueryId(callbackQuery.getId());
                answer.setShowAlert(true);
                if(allBackWater.compareTo(BigDecimal.ZERO) > 0){
                    currencyService.increase(user.getId(),UserType.USER, BusinessEnum.BACK_WATER,allBackWater, LocalDateTime.now(),"一键返水");
                    answer.setText("成功返水:" + allBackWater);
                    execute(answer);
                }else {
                    answer.setText("返水失败,请重新操作..待返水: 0");
                    execute(answer);
                }
            }

        } catch (TelegramApiException e) {
            e.printStackTrace();
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
    }


    public void sendMsg(Update update) {
        if (!update.getMessage().getFrom().getId().equals(update.getMessage().getChat().getId())){
            return;
        }
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();

        KeyboardButton keyboardButton1 = KeyboardButton.builder().text("开始游戏").build();

        KeyboardButton keyboardButton2 = KeyboardButton.builder().text("个人资料").build();
        KeyboardButton keyboardButton3 = KeyboardButton.builder().text("游戏报表").build();
        KeyboardButton keyboardButton4 = KeyboardButton.builder().text("获利查询").build();

        KeyboardButton keyboardButton5 = KeyboardButton.builder().text("USDT充值").build();
        KeyboardButton keyboardButton6 = KeyboardButton.builder().text("USDT提现").build();
        KeyboardButton keyboardButton7 = KeyboardButton.builder().text("绑定地址").build();

        KeyboardButton keyboardButton8 = KeyboardButton.builder().text("推广链接").build();
        KeyboardButton keyboardButton9 = KeyboardButton.builder().text("推广数据").build();
        KeyboardButton keyboardButton10 = KeyboardButton.builder().text("代理申请").build();


        keyboardRow.add(keyboardButton1);

        keyboardRow1.add(keyboardButton2);
        keyboardRow1.add(keyboardButton3);
        keyboardRow1.add(keyboardButton4);

        keyboardRow2.add(keyboardButton5);
        keyboardRow2.add(keyboardButton6);
        keyboardRow2.add(keyboardButton7);

        keyboardRow3.add(keyboardButton8);
        keyboardRow3.add(keyboardButton9);
        keyboardRow3.add(keyboardButton10);

        list.add(keyboardRow);
        list.add(keyboardRow1);
        list.add(keyboardRow2);
        list.add(keyboardRow3);
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
            Message messages = update.getMessage();
            User from = messages.getFrom();
            Chat chat = messages.getChat();
            com.tl.tgGame.project.entity.User user = userService.checkTgId(from.getId());
            if (text.contains("/start") && text.length() > 6) {
                String[] split = text.split(" ");
                String gameAccount = split[1];
                com.tl.tgGame.project.entity.User user1 = userService.queryByMemberAccount(gameAccount);
                if (user == null) {
                    user = userService.insertUser(from.getFirstName(), from.getLastName(), from.getUserName(),
                            from.getIsBot(), from.getId(), chat.getId().toString(),
                            Objects.nonNull(user1) ? user1.getId() : null, user1.getInviteChain());
                }
            } else {
                if (user == null) {
                    user = userService.insertUser(from.getFirstName(), from.getLastName(), from.getUserName(), from.getIsBot(),
                            from.getId(), chat.getId().toString(), null, null);
                }
            }
            if (StringUtils.isEmpty(text) || text.equals("/cancel") || text.contains("/start")) {
                SendMessage message3 = SendMessage.builder().text("\uD83D\uDD25祝您一路长虹，满载而归\uD83D\uDD25")
                        .replyMarkup(keyboardMarkup).chatId(update.getMessage().getChatId().toString())
                        .build();
                execute(message3);
            }
            if (text.equals("开始游戏")) {
                String beginGameLink = configService.get(ConfigConstants.BOT_BEGIN_GAME_GROUP_LINK);
                InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().url(beginGameLink).text("\uD83D\uDC9E游戏大厅").build();
                List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                inlineKeyboardButtons.add(inlineKeyboardButton1);
                InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();

                SendMessage sendMessage = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("请点击下方按钮开始您的游戏之旅").replyMarkup(inlineKeyboardMarkup).build();
                execute(sendMessage);
            }
            if (text.equals("个人资料")) {
                String gameRechargeKey = redisKeyGenerator.generateKey("GAME_RECHARGE", from.getId());
                String value = stringRedisTemplate.boundValueOps(gameRechargeKey).get();
                if (!org.springframework.util.StringUtils.isEmpty(value)) {
                    userService.gameWithdrawal(from.getId(), value);
                }
                InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().callbackData("个人资料:一键返水").text("一键返水").build();
                List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                inlineKeyboardButtons.add(inlineKeyboardButton1);
                InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();

                BotPersonInfo botPersonInfo = userService.getbotPersonInfo(user);
                StringBuilder builder = new StringBuilder();
                StringBuilder append = builder.append("游戏账号: ").append(botPersonInfo.getGameAccount()).append("\r\n")
                        .append("提现地址: ").append(botPersonInfo.getWithdrawalUrl()).append("\r\n")
                        .append("充值金额: ").append(botPersonInfo.getBalance()).append("\r\n")
                        .append("总充值: ").append(botPersonInfo.getAllRecharge()).append("\r\n")
                        .append("总提现: ").append(botPersonInfo.getAllWithdrawal()).append("\r\n")
                        .append("总返水: ").append(botPersonInfo.getAllBackWater()).append("\r\n")
                        .append("待返水: ").append(botPersonInfo.getWaitBackWater()).append("\r\n")
                        .append("总佣金: ").append(botPersonInfo.getAllCommission()).append("\r\n")
                        .append("提现待审核: ").append(botPersonInfo.getWaitAuthWithdrawal()).append("\r\n")
                        .append("\r\n")
                        .append(" 用户返水及推广佣金会因不同游戏设置不同比例，详情请点击“获利查询”查看详况 ").append("\r\n");
                SendMessage message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text(append.toString()).replyMarkup(inlineKeyboardMarkup).build();
                execute(message);
            }
            if (text.equals("游戏报表")) {
                BotGameStatisticsInfo gameStatisticsInfo = userService.getGameStatisticsInfo(user);
                StringBuilder append1 = new StringBuilder()
                        .append("-------当日报表--------").append("\r\n")
                        .append("总下注: ").append(gameStatisticsInfo.getDayBet()).append("\r\n")
                        .append("总派彩: ").append(gameStatisticsInfo.getDayFestoon()).append("\r\n")
                        .append("总盈利: ").append(gameStatisticsInfo.getDayProfit()).append("\r\n")
                        .append("\r\n")

                        .append("-------当周报表--------").append("\r\n")
                        .append("总下注: ").append(gameStatisticsInfo.getWeekBet()).append("\r\n")
                        .append("总派彩: ").append(gameStatisticsInfo.getWeekFestoon()).append("\r\n")
                        .append("总盈利: ").append(gameStatisticsInfo.getWeekProfit()).append("\r\n")
                        .append("\r\n")

                        .append("-------当月报表--------").append("\r\n")
                        .append("总下注: ").append(gameStatisticsInfo.getMonthBet()).append("\r\n")
                        .append("总派彩: ").append(gameStatisticsInfo.getMonthFestoon()).append("\r\n")
                        .append("总盈利: ").append(gameStatisticsInfo.getMonthProfit()).append("\r\n");

                SendMessage message1 = SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text(append1.toString()).build();
                execute(message1);
            }
            if (text.equals("获利查询")) {
                List<KeyboardRow> list1 = new ArrayList<>();
                KeyboardRow keyboardRow10 = new KeyboardRow();
                KeyboardRow keyboardRow11 = new KeyboardRow();
                KeyboardRow keyboardRow12 = new KeyboardRow();

                KeyboardButton keyboardButton11 = KeyboardButton.builder().text("\uD83D\uDC9EFC电子").build();
                KeyboardButton keyboardButton12 = KeyboardButton.builder().text("\uD83C\uDFB0WL棋牌").build();
                KeyboardButton keyboardButton13 = KeyboardButton.builder().text("\uD83D\uDC21EG电子").build();

                KeyboardButton keyboardButton14 = KeyboardButton.builder().text("\uD83D\uDC9EWL百家乐").build();
                KeyboardButton keyboardButton15 = KeyboardButton.builder().text("⚽\uFE0FWL体育").build();
                KeyboardButton keyboardButton16 = KeyboardButton.builder().text("\uD83C\uDF08FC捕鱼").build();

                KeyboardButton keyboardButton17 = KeyboardButton.builder().text("\uD83D\uDD3A返回上级\uD83D\uDD19").build();

                keyboardRow10.add(keyboardButton11);
                keyboardRow10.add(keyboardButton12);
                keyboardRow10.add(keyboardButton13);


                keyboardRow11.add(keyboardButton14);
                keyboardRow11.add(keyboardButton15);
                keyboardRow11.add(keyboardButton16);

                keyboardRow12.add(keyboardButton17);


                list1.add(keyboardRow10);
                list1.add(keyboardRow11);
                list1.add(keyboardRow12);
                ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder().keyboard(list1).resizeKeyboard(true).build();

                SendMessage message2 = SendMessage.builder()
                        .chatId(update.getMessage().getChatId().toString())
                        .text("尊贵的用户，请点击下列游戏查看 对应的返水、推广佣金比例及数据")
                        .replyMarkup(replyKeyboardMarkup)
                        .build();
                execute(message2);
            }
            if (text.equals("\uD83D\uDC9EFC电子") || text.equals("\uD83C\uDFB0WL棋牌") || text.equals("\uD83D\uDC21EG电子")
                    || text.equals("\uD83D\uDC9EWL百家乐") || text.equals("⚽\uFE0FWL体育") || text.equals("\uD83C\uDF08FC捕鱼")) {
                GameBusinessStatisticsInfo gameBusinessStatistics = userService.getGameBusinessStatistics(user, GameBusiness.pushName(text));
                StringBuilder append2 = new StringBuilder()
                        .append(text).append("\r\n")
                        .append("游戏名称: ").append(gameBusinessStatistics.getGameBusiness()).append("\r\n")
                        .append("返水比例: ").append(gameBusinessStatistics.getBackWaterRate()).append("\r\n")
                        .append("已返水: ").append(gameBusinessStatistics.getBackWater()).append("\r\n")
                        .append("待返水: ").append(gameBusinessStatistics.getWaitBackWater()).append("\r\n")
                        .append("下级佣金比例: ").append(gameBusinessStatistics.getJuniorCommissionRate()).append("\r\n")
                        .append("下级佣金: ").append(gameBusinessStatistics.getJuniorCommission()).append("\r\n");
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
            if (text.equals("USDT充值")) {
                SingleResponse<Address> userAddress = walletAPI.getUserAddress(user.getId());
                currencyService.getOrCreate(user.getId(), UserType.USER);
                String tron = userAddress.getData().getTron();
                String textToEncode = "tron"; // 要编码成二维码的文本
                String filePath = "qrcode.png"; // 生成的二维码图片文件路径
                int width = 300; // 图片宽度
                int height = 300; // 图片高度

                try {
                    // 设置二维码参数
                    Map<EncodeHintType, Object> hints = new HashMap<>();
                    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

                    // 生成二维码
                    BitMatrix bitMatrix = new MultiFormatWriter().encode(textToEncode, BarcodeFormat.QR_CODE, width, height, hints);

                    // 将BitMatrix转换为BufferedImage
                    BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
                    // 保存生成的二维码图片
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    ImageIO.write(image, "png", baos);

                    List<List<InlineKeyboardButton>> inlineKeyButtons = new ArrayList<>();
                    inlineKeyButtons.add(Collections.singletonList(InlineKeyboardButton.builder().url("https://t.me/cin89886").text("唯一充提财务").build()));
                    inlineKeyButtons.add(Collections.singletonList(InlineKeyboardButton.builder().callbackData("RECHARGE_CHECK").text("转账完成").build()));
                    SendPhoto sendPhoto = SendPhoto.builder()
                            .chatId(update.getMessage().getChatId())
                            .photo(new InputFile(new ByteArrayInputStream(baos.toByteArray()), filePath))
                            .caption("<b>充值地址：</b><code>" + tron + "</code>\n" +
                                    "\n" +
                                    "<b>⚠️尊贵的用户，充值金额需大于100USDT，否则充值将无法自动到账‼️</b>\n" +
                                    "\n" +
                                    "<b>⚠️充值地址，单击即可复制，请务必复制或输入正确的充值地址，否则造成的损失平台概不负责‼️</b>\n" +
                                    "\n" +
                                    "<b>⚠️转账成功后请您返回此页面，点击“转账完成”按钮，请务必完成此项操作，否则系统将无法为您自动充值‼️</b>" +
                                    "\n" +
                                    "如果您需要人工为您充值，请点击下方“唯一充提财务”按钮，我们将1对1为您提供人工充值服务。\n")
                            .parseMode("HTML")
                            .replyMarkup(InlineKeyboardMarkup.builder()
                                    .keyboard(inlineKeyButtons)
                                    .build()
                            ).build();
                    execute(sendPhoto);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
            if (text.equals("USDT提现") || checkState(update).equals("USDT提现")) {
                SendMessage message = null;
                List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder().url("https://t.me/cin89886").text("唯一充提财务").build();
                inlineKeyboardButtons.add(inlineKeyboardButton);
                InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(inlineKeyboardButtons).build();
                Currency currency = currencyService.getOrCreate(user.getId(), UserType.USER);
                if (update.getMessage().getText().equals("USDT提现")) {
                    buildState(update, true);
                    if (StringUtils.isEmpty(user.getWithdrawalUrl())) {
                        message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("尊敬的用户，请点击“绑定地址”按钮绑定充值提现地址").replyMarkup(inlineKeyboardMarkup).build();
                    } else {
                        StringBuilder append = new StringBuilder()
                                .append("可提现金额: ").append(currency.getRemain()).append("\r\n")
                                .append("尊贵的用户,请输入提现金额.").append("\r\n");
                        message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text(append.toString()).replyMarkup(inlineKeyboardMarkup).build();
                    }
                } else {
                    if (StringUtils.isEmpty(user.getWithdrawalUrl())) {
                        message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("尊敬的用户，请点击“绑定地址”按钮绑定充值提现地址").replyMarkup(inlineKeyboardMarkup).build();
                    } else if ((!NumberUtil.isParsable(update.getMessage().getText()) || !NumberUtil.isNumeric2(update.getMessage().getText()))
                            && !update.getMessage().getText().equals("/cancel")) {
                        message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("尊贵的用户，请正确输入金额").replyMarkup(inlineKeyboardMarkup).build();
                    } else if (Integer.parseInt(update.getMessage().getText()) < 20) {
                        message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text("尊敬的用户，可提现金额需大于20USDT").replyMarkup(inlineKeyboardMarkup).build();
                    } else if (new BigDecimal(update.getMessage().getText()).compareTo(currency.getRemain()) > 0) {
                        message = SendMessage.builder().chatId(update.getMessage().getChatId())
                                .text("尊贵的用户，您的余额不足，请重新输入。（输入 /cancel 取消）").replyMarkup(inlineKeyboardMarkup).build();
                    } else {
                        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
                        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();
                        InlineKeyboardButton inlineKeyboardButton1 = InlineKeyboardButton.builder().text("确认提现").callbackData("USDT提现:确认提现|" + update.getMessage().getText()).build();
                        inlineKeyboardButtonList.add(inlineKeyboardButton1);
                        lists.add(inlineKeyboardButtons);
                        lists.add(inlineKeyboardButtonList);
                        InlineKeyboardMarkup inlineKeyboardMarkup1 = InlineKeyboardMarkup.builder().keyboard(lists).build();
                        StringBuilder append = new StringBuilder()
                                .append("请确认提现").append(update.getMessage().getText()).append("\r\n");
                        message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                                .text(append.toString()).replyMarkup(inlineKeyboardMarkup1).build();
                    }
                }
                execute(message);
            }

            if (text.equals("绑定地址")) {
                SendMessage message = null;
                if (StringUtils.isEmpty(user.getWithdrawalUrl())) {
                    buildState(update, true);
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，请输入TRC-20地址进行提现绑定").build();
                } else {
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，您已成功绑定提现地址:" + user.getWithdrawalUrl()).build();
                }
                execute(message);
            } else if (checkState(update).equals("绑定地址")) {
                SendMessage message = null;
                if (text.length() != 34) {
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，请输入正确的TRC-20地址。").build();
                } else {
                    userService.update(new LambdaUpdateWrapper<com.tl.tgGame.project.entity.User>()
                            .set(com.tl.tgGame.project.entity.User::getWithdrawalUrl, text).eq(com.tl.tgGame.project.entity.User::getTgId, from.getId()));
                    message = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                            .text("尊贵的用户，您已成功绑定提现地址，如需变更，请联系本群唯一充提财务。").build();
                }
                execute(message);
            }
            if (text.equals("推广数据")) {
                BotExtendStatisticsInfo extendStatistics = userService.getExtendStatistics(user);
                StringBuilder append3 = new StringBuilder()
                        .append("-------下级用户--------").append("\r\n")
                        .append("人数: ").append(extendStatistics.getPeopleNumber()).append("\r\n")
                        .append("当日转码: ").append(extendStatistics.getTodayJoinGroup()).append("\r\n")
                        .append("当月转码: ").append(extendStatistics.getMonthJoinGroup()).append("\r\n")
                        .append("已结佣金: ").append(extendStatistics.getSettledCommission()).append("\r\n")
                        .append("当日总存款: ").append(extendStatistics.getTodayAllRecharge()).append("\r\n")
                        .append("当月总存款: ").append(extendStatistics.getMonthAllRecharge()).append("\r\n")
                        .append("当日总提款: ").append(extendStatistics.getTodayAllWithdrawal()).append("\r\n")
                        .append("当月总提款: ").append(extendStatistics.getMonthALlWithdrawal()).append("\r\n")
                        .append("当日总返水: ").append(extendStatistics.getTodayAllBackWater()).append("\r\n");
//                        .append("当日总彩金: ").append(extendStatistics.getTodayAllProfit()).append("\r\n")
//                        .append("当月总彩金: ").append(extendStatistics.getMonthAllProfit()).append("\r\n");
                SendMessage message7 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text(append3.toString()).build();
                execute(message7);
            }
            if (text.equals("推广链接")) {
                String link = configService.get(ConfigConstants.BOT_GROUP_INVITE_LINK);
                SendMessage message6 = SendMessage.builder().chatId(update.getMessage().getChatId().toString())
                        .text("全民推广，人人获利！\n" +
                                "\n" +
                                "邀请好友入群或将您的专属推广链接发送好友，好友点击入群即成为您的下级推广，下级投注均会自动返佣至您钱包余额！\n" +
                                "\n" +
                                "不同游戏对应不同返佣系数，详情请点击“下级数据”和“获利查询”查看\n" +
                                "\n" +
                                "尊贵的用户，您的推广链接为：" + link + "?start=" + user.getGameAccount()).build();
                execute(message6);
            }
            if(text.equals("代理申请")){
                InlineKeyboardMarkup build = InlineKeyboardMarkup.builder().keyboardRow(
                        Collections.singletonList(InlineKeyboardButton.builder()
                                .url("https://t.me/cin89886").text("唯一专属客服").build())).build();
                SendMessage message = SendMessage.builder().replyMarkup(build).text("尊贵的用户，请联系本群唯一专属客服，确认代理合作细节。")
                        .chatId(update.getMessage().getChatId()).build();
                execute(message);
            }
        } catch (TelegramApiException e) {
            log.error("个人中心机器人报错exception:{},输入文本text:{}", e, update.getMessage().getText());
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

