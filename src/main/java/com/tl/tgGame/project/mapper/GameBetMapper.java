package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.entity.GameBet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

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


}
