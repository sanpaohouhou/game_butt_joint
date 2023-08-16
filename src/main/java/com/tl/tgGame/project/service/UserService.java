package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.entity.User;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:40
 */
public interface UserService extends IService<User> {


    User insertUser(String firstName,String lastName,String username,Boolean isBot,Long tgId,String tgGroup);

    User checkGameAccount(String gameAccount);

    User checkTgIdAndGroup(Long tgId,String tgGroup);

    User queryByMemberAccount(String memberAccount);
}
