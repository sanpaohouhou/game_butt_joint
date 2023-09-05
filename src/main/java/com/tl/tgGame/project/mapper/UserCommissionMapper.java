package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.entity.UserCommission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/18 , 11:31
 */
public interface UserCommissionMapper extends BaseMapper<UserCommission> {

    @Select("SELECT IFNULL(SUM(`profit`), '0') FROM `user_commission` ${ew.customSqlSegment}")
    BigDecimal sumAmount(@Param(Constants.WRAPPER) Wrapper<UserCommission> wrapper);
}
