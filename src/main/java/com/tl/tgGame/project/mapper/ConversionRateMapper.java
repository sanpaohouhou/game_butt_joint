package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ConversionRateMapper {

    @Select("SELECT COUNT(DISTINCT `recharge`.`user_id`) FROM `user` JOIN `recharge` ON `user`.`id`=`recharge`.`user_id` ${ew.customSqlSegment}")
    Integer userChargeCount(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    @Select("SELECT COUNT(DISTINCT `game_bet`.`user_id`) FROM `user` JOIN `game_bet` ON `user`.`id`=`game_bet`.`user_id` ${ew.customSqlSegment}")
    Integer userBetCount(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
