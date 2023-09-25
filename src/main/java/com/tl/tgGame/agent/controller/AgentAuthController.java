package com.tl.tgGame.agent.controller;


import com.tl.tgGame.admin.role.dto.AdminChangePasswordDTO;
import com.tl.tgGame.admin.role.dto.AdminLoginDTO;
import com.tl.tgGame.auth.annotation.Uid;
import com.tl.tgGame.auth.aop.AgentRequired;
import com.tl.tgGame.auth.captcha.CaptchaService;
import com.tl.tgGame.auth.totp.service.TotpService;
import com.tl.tgGame.common.dto.Response;
import com.tl.tgGame.exception.ErrorEnum;
import com.tl.tgGame.project.entity.Agent;
import com.tl.tgGame.project.enums.UserType;
import com.tl.tgGame.project.service.AgentService;
import com.tl.tgGame.project.service.CurrencyService;
import com.tl.tgGame.util.Maps;
import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/agent/auth")
public class AgentAuthController {

    @Resource
    private CaptchaService captchaService;
    @Resource
    private AgentService agentService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private TotpService totpService;

    @PostMapping("/login")
    public Response login(@RequestBody @Valid AdminLoginDTO dto) throws QrGenerationException {
//        captchaService.verify(dto.getCode());
        return Response.success(Maps.newHashMap("token", agentService.login(dto.getUsername(), dto.getPassword())));
    }

    @PostMapping("/totp-login/step1")
    public Response loginStep1(@RequestBody @Valid AdminLoginDTO dto) throws QrGenerationException {
        captchaService.verify(dto.getCode());
        Agent partner = agentService.checkLogin(dto.getUsername(), dto.getPassword());
        return Response.success(totpService.step1(partner.getId(), partner.getUserName() + "@合伙人"));
    }
//
//    @PostMapping("/totp-login/step2")
//    public Response loginStep2(@RequestBody @Valid AdminLogin2DTO dto) {
//        return Response.success(totpService.step3(dto.getRefId(), dto.getCode()));
//    }

    @GetMapping("/my")
    @AgentRequired
    public Response my(@Uid Long id) {
        Agent agent = agentService.getById(id);
        return Response.success(agent);
    }

    @PostMapping("/change-password")
    @AgentRequired
    public Response changePassword(@Uid Long id, @RequestBody @Valid AdminChangePasswordDTO dto) {
        Agent agent = agentService.getById(id);
        if (!passwordEncoder.matches(dto.getOldPassword(), agent.getPassword())) {
            ErrorEnum.OLD_PASSWORD_ERROR.throwException();
        }
        agent.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        agentService.updateById(agent);
        return Response.success();
    }


}
