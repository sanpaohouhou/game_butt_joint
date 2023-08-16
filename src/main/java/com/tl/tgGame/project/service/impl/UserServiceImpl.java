package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.mapper.UserMapper;
import com.tl.tgGame.project.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public User insertUser(String firstName, String lastName, String username, Boolean isBot, Long tgId, String tgGroup) {
        String gameAccount = convertAccount();
        User user = checkGameAccount(gameAccount);
        if(user != null){
            ErrorEnum.USERNAME_ALREADY_USED.throwException();
        }
        User andGroup = checkTgIdAndGroup(tgId, tgGroup);
        if(andGroup != null){
            ErrorEnum.USERNAME_ALREADY_USED.throwException("");
        }
        User buildUser = buildUser(firstName, lastName, username, isBot, tgId, tgGroup, gameAccount);
        boolean save = save(buildUser);
        if(!save){
            ErrorEnum.SYSTEM_ERROR.throwException();
        }
        return buildUser;
    }

    @Override
    public User checkGameAccount(String gameAccount) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getGameAccount,gameAccount));
    }

    @Override
    public User checkTgIdAndGroup(Long tgId, String tgGroup) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getTgId,tgId).eq(User::getTgGroup,tgGroup));
    }

    @Override
    public User queryByMemberAccount(String memberAccount) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getGameAccount,memberAccount));
    }


    public static String convertAccount(){
        int anInt = new Random().nextInt(10);
        return "qu" + anInt;
    }

    public User buildUser(String firstName, String lastName, String username, Boolean isBot, Long tgId, String tgGroup,String gameAccount){
        return User.builder()
                .joinedTime(LocalDateTime.now())
                .firstName(firstName)
                .lastName(lastName)
                .gameAccount(gameAccount)
                .isBot(isBot)
                .tgId(tgId)
                .username(username)
                .tgGroup(tgGroup)
                .build();
    }

    public static void main(String[] args) {
        String s = convertAccount();
        System.out.println(s);
    }
}
