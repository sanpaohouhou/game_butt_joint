package com.tl.tgGame.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @auther w
 * @date 2023/8/30 , 10:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalUploadDTO {

    @NotNull
    private String image;
    @NotNull
    private String hash;
    @NotNull
    private Long id;
}
