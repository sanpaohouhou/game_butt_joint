package com.tl.tgGame.auth.aop;


import com.tl.tgGame.admin.role.entity.Admin;
import com.tl.tgGame.admin.role.entity.PermissionEnum;
import com.tl.tgGame.admin.role.service.AdminService;
import com.tl.tgGame.admin.role.service.RoleService;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Agent;
import com.tl.tgGame.project.entity.User;
import com.tl.tgGame.project.service.AgentService;
import com.tl.tgGame.project.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Aspect
@Component
public class AuthAspect {
    @Resource
    private AuthTokenService authTokenService;
    @Resource
    private AdminService adminService;
    @Resource
    private AgentService agentService;
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @Before("@annotation(adminRequired)")
    public void hasPermission(AdminRequired adminRequired) {
        Long uid = authTokenService.uid();

        Admin admin = adminService.getById(uid);
        if (admin == null) {
            ErrorEnum.NO_LOGIN.throwException();
        }
        // 超级管理员直接pass
        if (uid == 1L) {
            return;
        }
        if (admin.getEnable()) {
            List<String> permissionEnums = roleService.rolePermissions(admin.getRoleId());
            if (adminRequired.value().length > 0) {
                for (PermissionEnum it : adminRequired.value()) {
                    if (!permissionEnums.contains(it.name())) {
                        ErrorEnum.PERMISSION_MISS.throwException();
                    }
                }
                return;
            }
            if (adminRequired.or().length > 0) {
                for (PermissionEnum it : adminRequired.or()) {
                    if (permissionEnums.contains(it.name())) {
                        return;
                    }
                }
                ErrorEnum.PERMISSION_MISS.throwException();
            }
            return;
        }
        ErrorEnum.PERMISSION_MISS.throwException();
    }


    @Before("@annotation(h5UserRequired)")
    public void hasPermission(H5UserRequired h5UserRequired) {
        Long uid = authTokenService.uid();

        User user = userService.getById(uid);
        if (user != null) {
            return;
        }
        ErrorEnum.NO_LOGIN.throwException();
    }

    @Before("@annotation(agentRequired)")
    public void hasPermission(AgentRequired agentRequired) {
        Long uid = authTokenService.uid();

        Agent agent = agentService.getById(uid);
        if (agent != null) {
            return;
        }
        ErrorEnum.NO_LOGIN.throwException();
    }

}
