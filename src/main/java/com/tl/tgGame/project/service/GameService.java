package com.tl.tgGame.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.admin.dto.AdminGameReq;
import com.tl.tgGame.project.entity.Game;

/**
 * @version 1.0
 * @auther w
 * @date 2023/9/12 , 10:34
 */
public interface GameService extends IService<Game>{

    Game updateGame(Game game);

    Game updateStatus(Long gameId,Boolean status);

    Page<Game> queryByList(AdminGameReq req);

    Game queryByName(String gameName);


}
