package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.entity.Bet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:42
 */
public interface BetMapper extends BaseMapper<Bet> {

    @Select("SELECT IFNULL(SUM(`back_water_amount`), 0) FROM `bet` ${ew.customSqlSegment}")
    BigDecimal sumAmount(@Param(Constants.WRAPPER) Wrapper<Bet> wrapper);

    @Select("SELECT IFNULL(SUM(`bet`),0) FROM `bet` ${ew.customSqlSegment}")
    BigDecimal sumBetAmount(@Param(Constants.WRAPPER) Wrapper<Bet> wrapper);

    @Select("SELECT IFNULL(SUM(`win_lose`),0) FROM `bet` ${ew.customSqlSegment}")
    BigDecimal sumWinLose(@Param(Constants.WRAPPER) Wrapper<Bet> wrapper);

}
