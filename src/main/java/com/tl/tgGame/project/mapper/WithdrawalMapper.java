package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tl.tgGame.project.entity.Withdrawal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * usdt提现记录 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
public interface WithdrawalMapper extends BaseMapper<Withdrawal> {
    @Select("SELECT count(*) FROM usdt_withdrawal AS usdt  JOIN " +
            "market_merchant AS merchant ON usdt.uid = merchant.id ${ew.customSqlSegment}")
    int count(@Param(Constants.WRAPPER) Wrapper<Withdrawal> wrapper);

}
