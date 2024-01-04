package com.tl.tgGame.project.service.impl;

import com.tl.tgGame.project.dto.UserInfoStatistics;
import com.tl.tgGame.project.service.GameStatisticsService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 16:55
 */
@Service
public class GameStatisticsServiceImpl implements GameStatisticsService {


    @Override
    public UserInfoStatistics userInfoStatistics(Long userId) {
        UserInfoStatistics userInfoStatistics = new UserInfoStatistics();
        return userInfoStatistics;
    }
}
