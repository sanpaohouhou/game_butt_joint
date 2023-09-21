package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.admin.dto.UserExtendBetStatisticsDTO;
import com.tl.tgGame.project.dto.GameBetStatisticsListRes;
import com.tl.tgGame.project.entity.GameBet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/6 , 11:27
 */

@Mapper
public interface GameBetMapper extends BaseMapper<GameBet> {


    @Select("SELECT IFNULL(SUM(`back_water_amount`), '0') FROM `game_bet` ${ew.customSqlSegment}")
    BigDecimal sumAmount(@Param(Constants.WRAPPER) Wrapper<GameBet> wrapper);

    @Select("SELECT IFNULL(SUM(`bet`),'0') FROM `game_bet` ${ew.customSqlSegment}")
    BigDecimal sumBetAmount(@Param(Constants.WRAPPER) Wrapper<GameBet> wrapper);

    @Select("SELECT IFNULL(SUM(`profit`),'0') FROM `game_bet` ${ew.customSqlSegment}")
    BigDecimal sumWinLose(@Param(Constants.WRAPPER) Wrapper<GameBet> wrapper);

    @Select("SELECT COUNT(DISTINCT `user_id`) FROM `game_bet` ${ew.customSqlSegment}")
    Integer todayBetUserCount(@Param(Constants.WRAPPER) Wrapper<GameBet> wrapper);

    @Select("SELECT IFNULL(SUM(`profit`),'0') AS `allProfit`, IFNULL(SUM(`top_commission`),'0') AS `allTopCommission` " +
            " FROM `game_bet` AS gb JOIN `user`" +
            " u ON gb.user_id = u.id ${ew.customSqlSegment}")
    UserExtendBetStatisticsDTO extendBetStatistics(@Param(Constants.WRAPPER) Wrapper<?> wrapper);


    @Select("SELECT `game_business`, COUNT(*) AS betNumber,IFNULL(SUM(`bet`),'0') AS `betAmount`, " +
            " IFNULL(SUM(`profit`),'0') AS `userProfit`, " +
            " IFNULL(SUM(`valid_bet`),'0') AS `validAmount`,IFNULL(SUM(`top_commission`),'0') AS `userCommission`," +
            " IFNULL(SUM(`back_water_amount`),'0') AS backWaterAmount" +
            " FROM `game_bet` ${ew.customSqlSegment}")
    List<GameBetStatisticsListRes> betStatistics(@Param(Constants.WRAPPER) Wrapper<GameBet> wrapper);

    @Select("SELECT COUNT(*) AS betNumber,IFNULL(SUM(`bet`),'0') AS `betAmount`, " +
            " IFNULL(SUM(`profit`),'0') AS `userProfit`, " +
            " IFNULL(SUM(`valid_bet`),'0') AS `validAmount`,IFNULL(SUM(`top_commission`),'0') AS `userCommission` ," +
            " IFNULL(SUM(`back_water_amount`),'0') AS backWaterAmount" +
            " FROM `game_bet` ${ew.customSqlSegment}")
    GameBetStatisticsListRes userBetStatistics(@Param(Constants.WRAPPER) Wrapper<GameBet> wrapper);

}
