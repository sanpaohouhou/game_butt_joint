package com.tl.tgGame.auth.totp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TotpSecretMapper  extends BaseMapper<TotpSecret> {
}
