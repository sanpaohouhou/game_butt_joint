package com.tl.tgGame.project.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tl.tgGame.common.dto.PageObject;
import com.tl.tgGame.common.dto.Response;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 16:26
 */
@Data
@Builder
@NoArgsConstructor
public class GameApiResponse {

    private String Result = "0";
    private String ErrorText = "success";
    private Double MainPoints;

    public GameApiResponse(String Result, String ErrorText, Double MainPoints) {
        this.Result = Result;
        this.ErrorText = ErrorText;
        this.MainPoints = MainPoints;
    }

    public GameApiResponse(String Result, String ErrorText) {
        this.Result = Result;
        this.ErrorText = ErrorText;
    }

    public GameApiResponse(String Result,Double MainPoints){
        this.Result = Result;
        this.MainPoints = MainPoints;
    }

    public GameApiResponse(Double MainPoints) {
        this.MainPoints = MainPoints;
    }

    public static GameApiResponse success() {
        return new GameApiResponse(null);
    }
    public static GameApiResponse success(Double MainPoints) {
        return new GameApiResponse(MainPoints);
    }

    public static GameApiResponse error(String Result, String ErrorText) {
        return new GameApiResponse(Result, ErrorText);
    }
}
