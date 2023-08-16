package com.tl.tgGame.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tl.tgGame.admin.dto.AdminBetReq;
import com.tl.tgGame.admin.dto.AdminGameReq;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.project.entity.Bet;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.service.BetService;
import com.tl.tgGame.project.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/8 , 17:15
 */
@RestController
@RequestMapping("/api/admin/game")
public class AdminGameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private BetService betService;


    @GetMapping("gameList")
    public Response gameList(AdminGameReq req) {
        Page<Game> page = gameService.page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<Game>()
                .eq(Objects.nonNull(req.getGameName()), Game::getGameName, req.getGameName()));
        // TODO: 2023/8/8 封装对象
        return Response.pageResult(page);
    }


    @GetMapping("betList")
    public Response betList(AdminBetReq req) {
        Page<Bet> page = betService.page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<Bet>()
                .eq(Objects.nonNull(req.getGameName()), Bet::getGameName, req.getGameName())
                .ge(Objects.nonNull(req.getStartTime()), Bet::getCreateTime, req.getStartTime())
                .le(Objects.nonNull(req.getEndTime()), Bet::getCreateTime, req.getEndTime()));
        return Response.pageResult(page);
    }

}
