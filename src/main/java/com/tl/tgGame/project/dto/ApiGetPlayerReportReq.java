package com.tl.tgGame.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/9 , 14:56
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiGetPlayerReportReq {

    private String MemberAccount;

    private Integer LanguageID;
    /**
     * 指定是否显示玩家账号,0不显示,1显示
     */
    private Integer ShowAccount;

    private Integer GameType;

    private String RecordID;


}
