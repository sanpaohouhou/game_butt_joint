package com.tl.tgGame.admin.role.service;



import com.tl.tgGame.admin.role.dto.PermissionTree;
import com.tl.tgGame.admin.role.entity.PermissionEnum;

import java.util.List;

public interface PermissionService {
    /**
     * 返回过滤激活权限的权限树
     * @return List<PermissionTree>
     */
    List<PermissionTree> tree(PermissionEnum permission);

}