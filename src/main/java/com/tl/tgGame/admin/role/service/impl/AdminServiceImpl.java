package com.tl.tgGame.admin.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tl.tgGame.admin.role.entity.Admin;
import com.tl.tgGame.admin.role.mapper.AdminMapper;
import com.tl.tgGame.admin.role.service.AdminService;
import com.tl.tgGame.auth.service.AuthTokenService;
import com.tl.tgGame.exception.ErrorEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private AuthTokenService authTokenService;

    @Override
    public String login(String username, String password) {
        return authTokenService.login(checkLogin(username, password).getId()) ;
    }

    @Override
    public Admin checkLogin(String username, String password) {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            ErrorEnum.LOGIN_FAIL.throwException();
        }
        return admin;
    }

    @Override
    public void changePassword(long adminId, String newPassword) {
        Admin admin = getById(adminId);
        if (admin == null) {
            ErrorEnum.USER_NOT_JOIN.throwException();
        }
        admin.setPassword(passwordEncoder.encode(newPassword));
        updateById(admin);
    }

    @Override
    public Admin create(String username, String password, String note,Long roleId) {
        Admin admin = getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin != null) {
            ErrorEnum.USERNAME_ALREADY_USED.throwException();
        }
        Admin newAdmin = Admin.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .createTime(LocalDateTime.now())
                .note(note)
                .roleId(roleId)
                .build();
        save(newAdmin);
        return getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
    }
}
