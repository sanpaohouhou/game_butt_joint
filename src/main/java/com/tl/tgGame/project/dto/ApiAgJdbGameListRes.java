package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @version 1.0
 * @auther w
 * @date 2024/2/18 , 16:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiAgJdbGameListRes {

    private Map<String,String> name;
    private String status;
    private String game_code;
    private String rtp;
    private String[] device;
    private Map<String,String> pic_url;
    private String game_type;
}
