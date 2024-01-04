package com.tl.tgGame.admin.role.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminLogin2DTO {
    @NotBlank
    private String refId;
    @NotBlank
    private String code;
}
