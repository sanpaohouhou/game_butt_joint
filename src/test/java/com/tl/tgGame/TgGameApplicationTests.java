package com.tl.tgGame;

import cn.hutool.core.util.RandomUtil;
import com.tl.tgGame.project.dto.*;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.schedule.FcGameSchedule;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.GameService;
import com.tl.tgGame.project.service.UserService;
import com.tl.tgGame.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
//		loginReq.setGameID(21003);
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
		fcGameSchedule.insertGameRecord();
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

}
