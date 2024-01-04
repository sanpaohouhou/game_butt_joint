package com.tl.tgGame.admin.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tl.tgGame.admin.role.entity.Admin;

/**
 * <p>
 * 管理员 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
public interface AdminService extends IService<Admin> {
    /**
     * 通过用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     */
    String login(String username, String password);
    /**
     * 通过用户名密码尝试登录
     *
     * @param username 用户名
     * @param password 密码
     */
    Admin checkLogin(String username, String password);

    /**
     * 改密码
     *
     * @param adminId     管理员id
     * @param newPassword 密码
     */
    void changePassword(long adminId, String newPassword);

    /**
     * 创建管理员
     *
     * @param username 用户名
     * @param password 密码
     * @return admin
     */
    Admin create(String username, String password, String note,Long roleId);
}
