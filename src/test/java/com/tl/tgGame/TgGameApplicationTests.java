package com.tl.tgGame;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.EgBet;
import com.tl.tgGame.project.entity.GameBet;
import com.tl.tgGame.project.entity.WlBet;
import com.tl.tgGame.project.schedule.GameRecordSchedule;
import com.tl.tgGame.project.service.*;
import com.tl.tgGame.system.ConfigConstants;
import com.tl.tgGame.system.ConfigService;
import com.tl.tgGame.tgBot.service.BotMessageService;
import com.tl.tgGame.util.AESUtil;
import com.tl.tgGame.util.RedisKeyGenerator;
import com.tl.tgGame.wallet.WalletAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
class TgGameApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private ApiGameService apiGameService;

	@Test
	public void addMember(){
		ApiAddMemberReq addMemberReq = new ApiAddMemberReq();
		long anInt = new Random().nextLong() % 10000000000L;
		addMemberReq.setMemberAccount("pu" + anInt);
		apiGameService.addUser(addMemberReq);
//		gameService.testSign(addMemberReq);

	}

	@Test
	public void testGameLogin(){
		String anInt = "pu" + RandomUtil.randomLong(100000000000L);
		ApiLoginReq loginReq = new ApiLoginReq();
		loginReq.setMemberAccount("pu" + anInt);
		loginReq.setGameID(21008);
		loginReq.setLoginGameHall(true);
		loginReq.setLanguageID(2);
		String login = apiGameService.login(loginReq);
		System.out.println(login);
	}

	@Test
	public void kickOut(){
//		long randomLong = RandomUtil.randomLong(9999999999L);
//		ApiAddMemberReq req = new ApiAddMemberReq();
//		req.setMemberAccount("qu" + randomLong);
//		gameService.kickOut(req);
//		gameService.kickOutAll();
//		gameService.searchMember(req);
//		LocalDateTime localDateTime = TimeUtil.parseLocalDateTime("2023-08-24 09:40:00");
//		LocalDateTime localDateTime1 = TimeUtil.parseLocalDateTime("2023-08-24 09:55:00");
//		String s = TimeUtil.americaCharge(localDateTime);
//		String s1 = TimeUtil.americaCharge(localDateTime1);
//		ApiRecordListReq req = new ApiRecordListReq();
//		req.setStartDate(s);
//		req.setEndDate(s1);
//		List<ApiGameRecordListDTO> recordList = gameService.getRecordList(req);
//		Boolean aBoolean = betService.addBet(recordList);
//		System.out.println(recordList);
//		System.out.println(aBoolean);
//		fcGameSchedule.insertGameRecord();
	}

	@Autowired
	private GameRecordSchedule fcGameSchedule;


	@Autowired
	private UserService userService;

	@Autowired
	private BetService betService;

	@Test
	public void setPoints(){
//		long bigDecimal = RandomUtil.randomLong(9999999999L);
//		User user = userService.queryByMemberAccount("qu4");
		ApiSetPointReq req = new ApiSetPointReq();
		req.setAllOut(0);
		req.setMemberAccount("qu5617520236");
		req.setPoints(Double.valueOf("1200"));
		apiGameService.setPoints(req);

//		ApiRecordListReq req = new ApiRecordListReq();
//		req.setStartDate("2023-08-14 10:20:00");
//		req.setEndDate("2023-08-14 10:24:00");
//		gameService.getRecordList(req);
	}

	public static void main(String[] args) {
		Long anInt = new Random().nextLong() % 10000000000L;
		int v = (int) (Math.random() * 10000000000L);
		int anInt1 = new Random().nextInt(1000000000);
		long bigDecimal = RandomUtil.randomLong(9999999999L);
		System.out.println(anInt);
		System.out.println(anInt1);
		System.out.println(bigDecimal);
		System.out.println(v);
	}

	@Test
	public void egGameList(){
//		String host = "https://txmulti.api.helloholyfa.com/api/v1";
////		String url = host + "/389bet/" + "gameList";
////		HttpResponse execute = HttpUtil.createGet(url).execute();
////		String body = execute.body();
////		System.out.println(body);
//		ApiEgGameListRes apiEgGameListRes = gameService.egGameList();
//		System.out.println(apiEgGameListRes);
		ApiEgCreateUserReq req = ApiEgCreateUserReq.builder().merch("389bet_cny")
						.isBot(false).currency("CNY").playerId("1695008845821018114").build();
		apiGameService.egCreateUser(req);
		String json = new Gson().toJson(req);
		System.out.println(json);
//		String hash = Crypto.hmacToString(DigestFactory.createSHA256(), "pEd9heiqDR", json);
//		System.out.println(hash);
//		HttpResponse execute = HttpUtil.createPost(host + "/389bet/" + "createUser" + "?hash=" + hash).body(json).contentType("application/json").execute();
//		String body = execute.body();
//		System.out.println(body);
	}

	@Test
	public void egEnterGame(){
		ApiEgEnterGameReq req = new ApiEgEnterGameReq();
		req.setGameId("adventureofsinbad");
		req.setPlayerId("1695008845821018113");
		req.setMerch("389bet_cny");
		req.setLang("zh_CN");
		String s = apiGameService.egEnterGame(req);
		System.out.println(s);
	}

	@Test
	public void egGameRecord(){
		ApiEgRoundRecordTimeReq req = new ApiEgRoundRecordTimeReq();
		req.setGameId("adventureofsinbad");
		req.setPage("1");
		req.setPageSize("1000");
		req.setMerch("389bet_cny");
//		req.setPlayerId("1695008845821018113");
		req.setStart("1693376110349");
		req.setEnd(String.valueOf(System.currentTimeMillis()));
		apiGameService.egRoundRecordByTime(req);
	}

	@Test
	public void egDeposit(){
		ApiEgDepositReq req = new ApiEgDepositReq();
		req.setAmount("1000");
		req.setTransactionId(UUID.randomUUID().toString());
		req.setPlayerId("1695008845821018113");
		req.setMerch("389bet_cny");
		ApiEgDepositRes apiEgDepositRes = apiGameService.egDeposit(req);
		System.out.println(apiEgDepositRes);
	}


	@Test
	public void wlEnterGame(){

		String s = apiGameService.wlEnterGame(1695008845821018113L,null, request);
		System.out.println(s);
	}

	@Autowired
	private HttpServletRequest request;

	@Test
	public void aesEn(){

		try {
			String encrypt = AESUtil.encrypt("q.)0m7%qj3gzyr$?", "U81msjdw0v486i+ol8pko+0_(m0ck-p6%om!wbkmg0_(.34&1");
			String encrypt1 = AESUtil.encrypt("?)!!fvd*w)", "U81msjdw0v486i+ol8pko+0_(m0ck-p6%om!wbkmg0_(.34&1");
			String tests = AESUtil.dex("d62b8b8b9b1cfea3f5e95a534f4409ed793fff1445677490dc06ca19c0853552", "gjXayH1T3Jwxt/ee4TTsvw==");
			String testss = AESUtil.decrypt("d62b8b8b9b1cfea3f5e95a534f4409ed793fff1445677490dc06ca19c0853552", "gjXayH1T3Jwxt/ee4TTsvw==");
			AES aes = SecureUtil.aes(tests.getBytes(StandardCharsets.UTF_8));

			System.out.println(encrypt1);

			System.out.println(tests);
			System.out.println(testss);
			System.out.println(aes.encryptBase64(tests));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void wlPayOrder(){
		ApiWlGameRes b = apiGameService.wlPayOrder(1695008845821018113L, BigDecimal.valueOf(100));
		System.out.println(b);
	}

	@Test
	public void wlGameRecord(){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime localDateTime = now.minusDays(3);
		ApiWlGameResponse apiWlGameResponse = apiGameService.wlGameRecord(localDateTime, now);
		System.out.println(new Gson().toJson(apiWlGameResponse));
	}

	@Test
	public void egRecord(){
		fcGameSchedule.insertEgBetRecord();
	}

	@Test
	public void wlRecord(){
		fcGameSchedule.insertWlBetRecord();
	}

	@Autowired
	private EgBetService egBetService;
	@Test
	public void egCommission(){
		EgBet egBet = egBetService.getById(1698249505751683073L);
		Boolean aBoolean = egBetService.egCommission(egBet);
		System.out.println(aBoolean);
	}

	@Test
	public void fcRecord(){
		fcGameSchedule.insertFcBetRecord();
	}
	@Test
	public void fcCommission(){
		Bet byId = betService.getById(1698534705840705537L);
		betService.fcCommission(byId);
	}

	@Autowired
	private WlBetService wlBetService;
	@Test
	public void wlCommission(){
		WlBet wlBet = wlBetService.getById(1698272424892444674L);
		wlBetService.wlCommission(wlBet);
	}

	@Test
	public void withdrawal(){
		Boolean aBoolean = userService.gameWithdrawal(6585031559L, "EG");
		System.out.println(aBoolean);
	}

	@Autowired
	private RedisKeyGenerator redisKeyGenerator;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void testRedis(){
		String gameRechargeKey = redisKeyGenerator.generateKey("GAME_RECHARGE", "123");
//		stringRedisTemplate.boundValueOps(gameRechargeKey).set("FC");
		String value = stringRedisTemplate.boundValueOps(gameRechargeKey).get();
		System.out.println(value);
	}

	@Test
	public void egLogout(){

		ApiEgLogoutReq build = ApiEgLogoutReq.builder().playerId("qu4673689839").merch("389bet_usdt").build();
		Boolean aBoolean = apiGameService.egLogout(build);
		System.out.println(aBoolean);
	}

	@Test
	public void testErWeiMa(){
		String textToEncode = "亲爱的媳妇,我们结婚吧!想跟你创造一个属于我们幸福的家庭,我爱你!我滴宝"; // 要编码成二维码的文本
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
			File outputFile = new File(filePath);
			ImageIO.write(image, "png", outputFile);

			System.out.println("二维码已生成并保存到：" + filePath);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
		}
	}

	@Resource
	private BotMessageService botMessageService;

	@Resource
	private WithdrawalService withdrawalService;

	@Test
	public void testWw(){
		withdrawalService.audit(1701204820993638402L, true, "");
//		List<InlineKeyboardButton> keyboardButtons = Collections.singletonList(InlineKeyboardButton.builder().text("唯一充提财务").url("https://t.me/cin89886").build());
//		botMessageService.sendMessage("6245293274", "尊贵的用户，充值金额需大于100USDT，否则充值将无法自动到账！充值地址，单击即可复制，请务必复制或输入正确的充值地址，否则造成的损失平台概不负责！如果您需要人工为您充值，请点击下方“唯一充提财务”按钮，我们将1对1为您提供人工充值服务。感谢您的支持！",
//				InlineKeyboardMarkup.builder().keyboardRow(keyboardButtons).build()
//		);
//		botMessageService.sendMessage("6245293274", "hello", null);
	}


	@Autowired
	private ConfigService configService;

	@Test
	public void teamNumber(){
//		Integer integer = userService.teamNumber(1700046938802470914L);
//		System.out.println(integer);
		String wlParamKey = configService.getAndDecrypt(ConfigConstants.WL_PARAM_KEY);
		String wlReqKey = configService.getAndDecrypt(ConfigConstants.WL_REQ_KEY);
		System.out.println(wlReqKey);
		System.out.println(wlParamKey);
	}

	@Autowired
	private GameBetService gameBetService;
	@Test
	public void testCommission(){
		Long gameId = 1704453532604534785L;
		GameBet gameBet = gameBetService.getById(gameId);
		gameBetService.loseCommission(gameBet);
	}


}
