package com.tl.tgGame.admin.role.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddAdminDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String note;
    private Long roleId;
}
