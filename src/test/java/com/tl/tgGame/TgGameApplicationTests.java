package com.tl.tgGame;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.schedule.FcGameSchedule;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.util.AESUtil;
import com.tl.tgGame.util.TimeUtil;
import com.tl.tgGame.util.crypto.Crypto;
import io.vertx.core.http.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.bouncycastle.crypto.util.DigestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static cn.hutool.crypto.digest.HmacAlgorithm.HmacSHA256;

@SpringBootTest
class TgGameApplicationTests {

	@Test
	void contextLoads() {
	}


	@Autowired
	private GameService gameService;

	@Test
	public void addMember(){
		ApiAddMemberReq addMemberReq = new ApiAddMemberReq();
		long anInt = new Random().nextLong() % 10000000000L;
		addMemberReq.setMemberAccount("pu" + anInt);
		gameService.addUser(addMemberReq);
//		gameService.testSign(addMemberReq);

	}

	@Test
	public void testGameLogin(){
		String anInt = "pu" + RandomUtil.randomLong(100000000000L);
		ApiLoginReq loginReq = new ApiLoginReq();
		loginReq.setMemberAccount("pu" + anInt);
		loginReq.setGameID(21003);
		loginReq.setLoginGameHall(true);
		loginReq.setLanguageID(2);
		String login = gameService.login(loginReq);
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
	private FcGameSchedule fcGameSchedule;


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
		gameService.setPoints(req);

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
		ApiEgGameListRes apiEgGameListRes = gameService.egGameList();
		System.out.println(apiEgGameListRes);
//		ApiEgCreateUserReq req = ApiEgCreateUserReq.builder().merch("389bet_cny")
//						.isBot(false).currency("CNY").playerId("1695008845821018114").build();
//		gameService.egCreateUser(req);
//		String json = new Gson().toJson(req);
//		System.out.println(json);
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
		String s = gameService.egEnterGame(req);
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
		gameService.egRoundRecordByTime(req);
	}

	@Test
	public void egDeposit(){
		ApiEgDepositReq req = new ApiEgDepositReq();
		req.setAmount("1000");
		req.setTransactionId(UUID.randomUUID().toString());
		req.setPlayerId("1695008845821018113");
		req.setMerch("389bet_cny");
		ApiEgDepositRes apiEgDepositRes = gameService.egDeposit(req);
		System.out.println(apiEgDepositRes);
	}


	@Test
	public void wlEnterGame(){

		String s = gameService.wlEnterGame(1695008845821018113L,null, request);
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
		ApiWlGameRes b = gameService.wlPayOrder(1695008845821018113L, BigDecimal.valueOf(100));
		System.out.println(b);
	}

	@Test
	public void wlGameRecord(){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime localDateTime = now.minusMinutes(15);
		ApiWlGameResponse apiWlGameResponse = gameService.wlGameRecord(localDateTime, now);
		System.out.println(new Gson().toJson(apiWlGameResponse));
	}



}
