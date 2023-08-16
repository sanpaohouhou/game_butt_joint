package com.tl.tgGame.admin.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.admin.role.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    Page<Role> roleList(int page, int size);

    void setActive(long roleId, boolean active);

    Role create(String roleName, String remark, List<String> codes);

    Role update(long id, String roleName, String remark, List<String> codes);

    void delete(long roleId);

    List<String> rolePermissions(Long roleId);
}
