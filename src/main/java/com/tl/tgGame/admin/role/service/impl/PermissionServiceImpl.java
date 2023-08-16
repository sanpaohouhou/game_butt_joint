package com.tl.tgGame.admin.role.service.impl;



import com.tl.tgGame.admin.role.dto.PermissionTree;
import com.tl.tgGame.admin.role.entity.PermissionEnum;
import com.tl.tgGame.admin.role.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Override
    public List<PermissionTree> tree(PermissionEnum permissionEnum) {
        List<PermissionTree> permissionTree = new ArrayList<>();
        if (permissionEnum != null) {
            for (PermissionEnum i : PermissionEnum.values()) {
                if (i.getParent() != null && i.getParent().equals(permissionEnum)) {
                    permissionTree.add(PermissionTree.builder().code(i.name()).permissionName(i.getPermissionName()).children(tree(i)).build());
                }
            }
        }else {
            for (PermissionEnum i : PermissionEnum.values()) {
                if (i.getParent() == null) {
                    permissionTree.add(PermissionTree.builder().code(i.name()).permissionName(i.getPermissionName()).children(tree(i)).build());
                }
            }
        }
        return permissionTree;
    }
}
