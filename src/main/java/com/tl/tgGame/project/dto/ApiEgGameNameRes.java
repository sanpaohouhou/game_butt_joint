package com.tl.tgGame.project.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/29 , 17:06
 */
@Data
public class ApiEgGameNameRes {

    private String gameId;
    private Map<String,String> name;
}
