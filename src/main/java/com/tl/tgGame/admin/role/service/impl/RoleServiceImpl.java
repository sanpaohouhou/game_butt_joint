package com.tl.tgGame.admin.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.admin.role.entity.Role;
import com.tl.tgGame.admin.role.mapper.RoleMapper;
import com.tl.tgGame.admin.role.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public Page<Role> roleList(int page, int size) {
        return page(new Page<>(page, size), new LambdaQueryWrapper<Role>().orderByDesc(Role::getId));
    }

    @Override
    public void setActive(long roleId, boolean active) {
        Role role = getById(roleId);
        if (role != null) {
            role.setActive(active);
            updateById(role);
        }
    }

    @Override
    public Role create(String roleName, String remark, List<String> codes) {
        Role role = Role.builder()
                .roleName(roleName)
                .remark(remark)
                .active(true)
                .createdTime(System.currentTimeMillis()).build();
        role.setCodes(codes);
        save(role);
        return role;
    }

    @Override
    public Role update(long id, String roleName, String remark, List<String> codes) {
        Role role = getById(id);
        if (role != null) {
            role.setRoleName(roleName);
            role.setRemark(remark);
            role.setCodes(codes);
            updateById(role);
        }
        return role;
    }

    @Override
    public void delete(long id) {
        removeById(id);
    }

    @Override
    public List<String> rolePermissions(Long roleId) {
        List<String> permissionEnums = new ArrayList<>();
        Role role = getById(roleId);
        if (role != null && role.getActive()) {
            permissionEnums.addAll(role.getCodes());
        }
        return permissionEnums;
    }
}
