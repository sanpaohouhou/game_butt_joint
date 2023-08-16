package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.project.entity.UserProfit;
import com.tl.tgGame.project.mapper.UserProfitMapper;
import com.tl.tgGame.project.service.UserProfitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/16 , 11:26
 */
@Slf4j
@Service
public class UserProfitServiceImpl extends ServiceImpl<UserProfitMapper, UserProfit> implements UserProfitService {
}
