package com.tl.tgGame.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tl.tgGame.project.entity.Currency;
import com.tl.tgGame.project.enums.UserType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 * 用户余额表 Mapper 接口
 * </p>
 *
 * @author hd
 * @since 2020-12-04
 */
@Mapper
public interface CurrencyMapper extends BaseMapper<Currency> {

    @Update("UPDATE `currency` SET `balance`=`balance`+#{amount},`remain`=`remain`+#{amount} WHERE `uid`=#{uid} AND `user_type`=#{type}")
    long increase(@Param("uid") long uid, @Param("type") UserType type, @Param("amount") BigDecimal amount);

    @Update("UPDATE `currency` SET `balance`=`balance`-#{amount},`freeze`=`freeze`-#{amount} WHERE `uid`=#{uid} AND `user_type`=#{type} AND `freeze`>=#{amount} ")
    long reduce(@Param("uid") long uid, @Param("type") UserType type, @Param("amount") BigDecimal amount);

    @Update("UPDATE `currency` SET `balance`=`balance`-#{amount},`remain`=`remain`-#{amount} WHERE `uid`=#{uid} AND `user_type`=#{type} AND `remain`>=#{amount} ")
    long withdraw(@Param("uid") long uid, @Param("type") UserType type, @Param("amount") BigDecimal amount);

    @Update("UPDATE `currency` SET `balance`=`balance`-#{amount},`remain`=`remain`-#{amount} WHERE `uid`=#{uid} AND `user_type`=#{type} ")
    long withdrawForce(@Param("uid") long uid, @Param("type") UserType type, @Param("amount") BigDecimal amount);

    @Update("UPDATE `currency` SET `freeze`=`freeze`+#{amount},`remain`=`remain`-#{amount} WHERE `uid`=#{uid} AND `user_type`=#{type} AND `remain`>=#{amount} ")
    long freeze(@Param("uid") long uid, @Param("type") UserType type, @Param("amount") BigDecimal amount);

    @Update("UPDATE `currency` SET `freeze`=`freeze`-#{amount},`remain`=`remain`+#{amount} WHERE `uid`=#{uid} AND `user_type`=#{type} AND `freeze`>=#{amount} ")
    long unfreeze(@Param("uid") long uid, @Param("type") UserType type, @Param("amount") BigDecimal amount);
}
