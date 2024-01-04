package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.entity.Withdrawal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * usdt提现记录 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
public interface WithdrawalMapper extends BaseMapper<Withdrawal> {
    @Select("SELECT IFNULL(SUM(`amount`),0) FROM `withdrawal` ${ew.customSqlSegment}")
    BigDecimal sumWithdrawal(@Param(Constants.WRAPPER) Wrapper<Withdrawal> wrapper);

    @Select(" SELECT COUNT(DISTINCT `uid`) FROM `withdrawal` AS wi JOIN `user` u ON wi.uid = u.id ${ew.customSqlSegment}")
    Long countJuniorWithdrawalNumber(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    @Select(" SELECT IFNULL(SUM(`amount`),'0') FROM `withdrawal` AS wi JOIN `user` u ON wi.uid = u.id ${ew.customSqlSegment}")
    BigDecimal sumJuniorRechargeAmount(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
