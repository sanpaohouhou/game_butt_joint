package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.project.dto.BotExtendStatisticsInfo;
import com.tl.tgGame.project.dto.BotGameStatisticsInfo;
import com.tl.tgGame.project.dto.BotPersonInfo;
import com.tl.tgGame.project.dto.GameBusinessStatisticsInfo;
import com.tl.tgGame.project.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 17:40
 */
public interface UserService extends IService<User> {


    User insertUser(String firstName,String lastName,String username,Boolean isBot,Long tgId,String tgGroup);

    User checkGameAccount(String gameAccount);

    User checkTgIdAndGroup(Long tgId,String tgGroup);

    User checkTgId(Long tgId);

    User queryByMemberAccount(String memberAccount);

    BotPersonInfo getbotPersonInfo(User user);

    BotGameStatisticsInfo getGameStatisticsInfo(User user);

    GameBusinessStatisticsInfo getGameBusinessStatistics(User user,String gameBusiness);

    BotExtendStatisticsInfo getExtendStatistics(User user);

    Boolean updateByHasGroup(Long tgId,String tgGroup,Boolean hasGroup);

    Boolean fcGameRecharge(Long tgId);

    Boolean fcGameWithdrawal(Long tgId);

    List<User> queryByInviteUser(Long inviteUser, LocalDateTime startTime, LocalDateTime endTime);


}
