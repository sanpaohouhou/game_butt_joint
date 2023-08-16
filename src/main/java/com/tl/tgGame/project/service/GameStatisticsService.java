package com.tl.tgGame.project.service;

import com.tl.tgGame.project.dto.UserInfoStatistics;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/4 , 16:54
 */
public interface GameStatisticsService {

    UserInfoStatistics userInfoStatistics(Long userId);
}
