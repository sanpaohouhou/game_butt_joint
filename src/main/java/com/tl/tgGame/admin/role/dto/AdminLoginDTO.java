package com.tl.tgGame.admin.role.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminLoginDTO {
//    @NotBlank
//    private String loginNo;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String code;
}
