package com.tl.tgGame.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/7 , 14:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExtend {

    private Long id;

    private Long fromUserId;

    private Long toUserId;

    private String gameAccount;

    private Integer extendNumber;

    private String extendUrl;

    private LocalDateTime createTime;
}
