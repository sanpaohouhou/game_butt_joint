package com.tl.tgGame.project.controller;

import com.tl.tgGame.project.dto.PgResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @auther w
 * @date 2024/1/23 , 10:15
 */
@RestController
@RequestMapping("")
public class ApiCommonController {



    @PostMapping("/verifySession")
    public PgResponse verifySession(@RequestParam String trace_id,@RequestParam String operator_token,
                                    @RequestParam String secret_key,@RequestParam String operator_player_session){
        return new PgResponse();
    }
}
