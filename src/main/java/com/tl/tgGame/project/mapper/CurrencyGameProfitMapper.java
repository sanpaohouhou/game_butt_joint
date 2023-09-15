package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.dto.GameBackWaterRes;
import com.tl.tgGame.project.entity.CurrencyGameProfit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/12 , 19:23
 */
@Mapper
public interface CurrencyGameProfitMapper extends BaseMapper<CurrencyGameProfit> {

    @Update("UPDATE `currency_game_profit` SET `balance`=`balance`+#{amount} WHERE `user_id`=#{uid} and `game_business` = #{type}")
    long increase(@Param("uid") long uid, @Param("type") String type, @Param("amount") BigDecimal amount);
    @Update("UPDATE `currency_game_profit` SET `balance`=`balance`-#{amount} ,`settled` = `settled` + #{amount} WHERE `user_id`=#{uid} AND `game_business`=#{type} AND `balance`>=#{amount} ")
    long withdraw(@Param("uid") long uid, @Param("type") String type, @Param("amount") BigDecimal amount);

    @Select(" SELECT IFNULL(SUM(`balance`),'0') AS allWaitBackWater , " +
            " IFNULL(SUM(`settled`),'0') AS allBackWater " +
            " FROM `currency_game_profit` AS cgp JOIN `user` u ON " +
            " cgp.user_id = u.id  ${ew.customSqlSegment}")
    GameBackWaterRes juniorGameBackWaterStatistics(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    @Select("SELECT `game_business` AS gameBusiness , IFNULL(SUM(`balance`),'0') AS allWaitBackWater ," +
            " IFNULL(SUM(`settled`),'0') AS allBackWater " +
            " FROM `currency_game_profit` ${ew.customSqlSegment}")
    GameBackWaterRes userBackWater(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
