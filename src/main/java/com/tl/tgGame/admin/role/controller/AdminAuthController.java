package com.tl.tgGame.admin.role.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.tl.tgGame.admin.role.dto.AddAdminDTO;
import com.tl.tgGame.admin.role.dto.AdminLogin2DTO;
import com.tl.tgGame.admin.role.entity.Admin;
import com.tl.tgGame.admin.role.entity.PermissionEnum;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.auth.aop.AdminRequired;
import com.tl.tgGame.common.dto.PageObject;
import dev.samstevens.totp.exceptions.QrGenerationException;
import com.tl.tgGame.admin.role.dto.AdminLoginDTO;
import com.tl.tgGame.admin.role.service.AdminService;
import com.tl.tgGame.auth.captcha.CaptchaService;
import com.tl.tgGame.auth.totp.service.TotpService;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.util.Maps;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>
 * 管理员 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {
    @Resource
    private AdminService adminService;
    @Resource
    private CaptchaService captchaService;
    @Resource
    private TotpService totpService;

    @PostMapping("/login")
    public Response login(@RequestBody @Valid AdminLoginDTO dto)
//            throws QrGenerationException {
    {
        captchaService.verify(dto.getCode());
        return Response.success(Maps.newHashMap("token", adminService.login(dto.getUsername(), dto.getPassword())));
    }

    @PostMapping("/totp-login/step1")
    public Response loginStep1(@RequestBody @Valid AdminLoginDTO dto) throws QrGenerationException {
        captchaService.verify(dto.getCode());
        Admin admin = adminService.checkLogin(dto.getUsername(), dto.getPassword());
        return Response.success(totpService.step1(admin.getId(), admin.getUsername() + "@管理员"));
    }

    @PostMapping("/totp-login/step2")
    public Response loginStep2(@RequestBody @Valid AdminLogin2DTO dto) {
        return Response.success(totpService.step2(dto.getRefId(), dto.getCode()));
    }

    @PostMapping("/change-password")
    @AdminRequired
    public Response changePassword(@Uid Long id, String newPassword) {
        adminService.changePassword(id, newPassword);
        return Response.success();
    }

    @PostMapping("/add-admin")
    @AdminRequired(PermissionEnum.ADMIN_MANAGE)
    public Response addAdmin(@RequestBody @Valid AddAdminDTO dto) {
        return Response.success(adminService.create(dto.getUsername(), dto.getPassword(), dto.getNote(),dto.getRoleId()));
    }

    @PostMapping("/{adminId}/enable")
    @AdminRequired(PermissionEnum.ADMIN_MANAGE)
    public Response enableAdmin(@PathVariable Long adminId, boolean enable) {
        Admin admin = adminService.getById(adminId);
        if (admin != null) {
            admin.setEnable(enable);
            adminService.updateById(admin);
        }
        return Response.success();
    }

    @DeleteMapping("/{adminId}")
    @AdminRequired(PermissionEnum.ADMIN_MANAGE)
    public Response deleteAdmin(@PathVariable Long adminId) {
        adminService.removeById(adminId);
        return Response.success();
    }

    @GetMapping("/admins")
    @AdminRequired(PermissionEnum.ADMIN_MANAGE)
    public Response getAdmins(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String username) {
        return Response.success(PageObject.of(adminService.page(new Page<>(page, size),
                new LambdaQueryWrapper<Admin>().eq(Objects.nonNull(username), Admin::getUsername, username).orderByDesc(Admin::getId)
        )));
    }

    @PostMapping("/{adminId}/reset-password")
    @AdminRequired(PermissionEnum.ADMIN_MANAGE)
    public Response resetAdminPassword(
            @PathVariable Long adminId, String newPassword) {
        adminService.changePassword(adminId, newPassword);
        return Response.success();
    }

    @GetMapping("/my")
    @AdminRequired
    public Response my(@Uid Long id) {
        return Response.success(adminService.getById(id));
    }

}
