package com.tl.tgGame.admin.role.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PermissionTree {
    private String code;

    private String permissionName;

    private List<PermissionTree> children;
}