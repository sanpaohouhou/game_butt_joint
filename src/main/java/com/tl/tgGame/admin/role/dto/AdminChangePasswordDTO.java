package com.tl.tgGame.admin.role.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminChangePasswordDTO {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
