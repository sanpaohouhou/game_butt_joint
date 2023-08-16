package com.tl.tgGame.admin.controller;

import com.tl.tgGame.admin.dto.AdminGameBetStatistics;
import com.tl.tgGame.admin.dto.AdminHomeUserStatistics;
import com.tl.tgGame.admin.dto.UserOverviewStatistics;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 15:51
 */
@RestController
@RequestMapping("/api/admin/home")
public class AdminHomeController {

    @Autowired
    private UserService userService;

    /**
     * 用户统计
     * @return
     */
    @GetMapping("homeUserStatistics")
    public Response homeUserStatistics(){
        AdminHomeUserStatistics adminHomeUserStatistics = new AdminHomeUserStatistics();
        return Response.success(adminHomeUserStatistics);
    }

    /**
     * 用户转化漏斗
     * @param type
     * @return
     */
    @GetMapping("userOverviewStatistics")
    public Response userOverviewStatistics(Integer type){
        UserOverviewStatistics userOverviewStatistics = new UserOverviewStatistics();
        return Response.success(userOverviewStatistics);
    }

    /**
     * 游戏下注
     * @return
     */
    @GetMapping("gameBet")
    public Response gameBet(){
        AdminGameBetStatistics adminGameBetStatistics = new AdminGameBetStatistics();
        return Response.success(adminGameBetStatistics);
    }

}
