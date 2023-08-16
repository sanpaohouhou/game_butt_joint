package com.tl.tgGame.auth.aop;


import com.tl.tgGame.auth.service.AuthTokenService;
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
//    @Resource
//    private IAdminService adminService;
//    @Resource
//    private IPartnerService partnerService;
//    @Resource
//    private IKeeperService keeperService;
//    @Resource
//    private IUserService userService;
//    @Resource
//    private RoleService roleService;

//    @Before("@annotation(adminRequired)")
//    public void hasPermission(AdminRequired adminRequired) {
//        Long uid = authTokenService.uid();
//
//        Admin admin = adminService.getById(uid);
//        if (admin == null) {
//            ErrorEnum.NO_LOGIN.throwException();
//        }
//        // 超级管理员直接pass
//        if (uid == 1L) {
//            return;
//        }
//        if (admin.getEnable()) {
//            List<String> permissionEnums = roleService.rolePermissions(admin.getRoleId());
//            if (adminRequired.value().length > 0) {
//                for (PermissionEnum it : adminRequired.value()) {
//                    if (!permissionEnums.contains(it.name())) {
//                        ErrorEnum.PERMISSION_MISS.throwException();
//                    }
//                }
//                return;
//            }
//            if (adminRequired.or().length > 0) {
//                for (PermissionEnum it : adminRequired.or()) {
//                    if (permissionEnums.contains(it.name())) {
//                        return;
//                    }
//                }
//                ErrorEnum.PERMISSION_MISS.throwException();
//            }
//            return;
//        }
//        ErrorEnum.PERMISSION_MISS.throwException();
//    }


    @Before("@annotation(h5UserRequired)")
    public void hasPermission(H5UserRequired h5UserRequired) {
//        Long uid = authTokenService.uid();
//
//        User user = userService.getById(uid);
//        if (user != null && user.getOrigin().equals(UserOrigin.H5)) {
//            return;
//        }
//        ErrorEnum.NO_LOGIN.throwException();
    }

}
