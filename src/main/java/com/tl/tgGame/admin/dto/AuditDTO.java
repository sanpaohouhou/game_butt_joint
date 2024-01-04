package com.tl.tgGame.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuditDTO {
    @NotNull
    private Boolean result;
    @NotNull
    private Long id;

    private String note;

    private String image;
}
