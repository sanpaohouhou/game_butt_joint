package com.tl.tgGame.project.dto;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/9 , 14:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiLoginReq {

    private String MemberAccount;

    private Integer GameID;

    private Integer LanguageID;

    private String HomeUrl;

    private Boolean JackpotStatus;

    private Boolean LoginGameHall;

    private JsonArray GameHallGameType;
}
