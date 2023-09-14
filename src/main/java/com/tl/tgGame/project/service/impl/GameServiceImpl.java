package com.tl.tgGame.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tl.tgGame.admin.dto.AdminGameReq;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Game;
import com.tl.tgGame.project.mapper.GameMapper;
import com.tl.tgGame.project.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/12 , 10:40
 */
@Slf4j
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    @Override
    public Game updateGame(Game game) {
        Game game1 = getById(game.getId());
        if(game1 == null){
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        game1.setBackWaterRate(game.getBackWaterRate());
        game1.setTopCommissionRate(game.getTopCommissionRate());
        game1.setUpdateTime(LocalDateTime.now());
        updateById(game1);
        return game1;
    }

    @Override
    public Game updateStatus(Long gameId, Boolean status) {
        Game game = getById(gameId);
        if(game == null){
            ErrorEnum.OBJECT_NOT_FOUND.throwException();
        }
        game.setStatus(status);
        game.setUpdateTime(LocalDateTime.now());
        updateById(game);
        return game;
    }

    @Override
    public Page<Game> queryByList(AdminGameReq req) {
        return page(new Page<>(req.getPage(), req.getSize()), new LambdaQueryWrapper<Game>()
                .eq(Objects.nonNull(req.getGameName()), Game::getGameName, req.getGameName()));
    }

    @Override
    public Game queryByName(String gameName) {
        return getOne(new LambdaQueryWrapper<Game>().eq(Game::getGameName,gameName).eq(Game::getStatus,1));
    }
}
