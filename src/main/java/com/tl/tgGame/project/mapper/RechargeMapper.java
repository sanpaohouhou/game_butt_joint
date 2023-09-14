package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.entity.Recharge;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 用户充值提现记录表 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
public interface RechargeMapper extends BaseMapper<Recharge> {

    @Select("SELECT IFNULL(SUM(`amount`), 0) FROM `recharge` ${ew.customSqlSegment}")
    BigDecimal amountSum(@Param(Constants.WRAPPER) Wrapper<Recharge> wrapper);

    @Select("SELECT COUNT(DISTINCT `user_id`) FROM `recharge` ${ew.customSqlSegment}")
    Integer countRechargeNumber(@Param(Constants.WRAPPER) Wrapper<Recharge> wrapper);

    @Select(" SELECT COUNT(DISTINCT `user_id`) FROM `recharge` AS re JOIN `user` u ON re.user_id = u.id ${ew.customSqlSegment}")
    Integer countJuniorRechargeNumber(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    @Select(" SELECT IFNULL(SUM(`amount`),'0') FROM `recharge` AS re JOIN `user` u ON re.user_id = u.id ${ew.customSqlSegment}")
    BigDecimal sumJuniorRechargeAmount(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
