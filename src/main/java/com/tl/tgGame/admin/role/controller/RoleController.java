package com.tl.tgGame.admin.role.controller;



import com.tl.tgGame.admin.role.entity.PermissionEnum;
import com.tl.tgGame.admin.role.param.AddRoleParam;
import com.tl.tgGame.admin.role.param.SetActiveRoleParam;
import com.tl.tgGame.admin.role.param.UpdateRoleParam;
import com.tl.tgGame.admin.role.service.PermissionService;
import com.tl.tgGame.admin.role.service.RoleService;
import com.tl.tgGame.auth.aop.AdminRequired;
import com.tl.tgGame.common.dto.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin/role")
public class RoleController {
    @Resource
    PermissionService permissionService;
    @Resource
    RoleService roleService;

    @GetMapping("/list")
    @AdminRequired(PermissionEnum.CONFIG_MANAGE)
    public Response roleList(@RequestParam(required = false, defaultValue = "1") int page,
                             @RequestParam(required = false, defaultValue = "20") int size) {
        return Response.pageResult(roleService.roleList(page, size));
    }

    @GetMapping("/{id}")
    public Response roleGet(@PathVariable Long id) {
        return Response.success(roleService.getById(id));
    }

    @GetMapping("/tree")
    @AdminRequired
    public Response roleTree(@RequestParam(required = false) PermissionEnum code) {
        return Response.success(permissionService.tree(code));
    }

    @PostMapping("/create")
    @AdminRequired(PermissionEnum.CONFIG_MANAGE)
    public Response create(@RequestBody @Valid AddRoleParam param) {
        return Response.success(roleService.create(param.getRoleName(), param.getRemark(), param.getCodes()));
    }

    @PostMapping("/update")
    @AdminRequired(PermissionEnum.CONFIG_MANAGE)
    public Response update(@RequestBody @Valid UpdateRoleParam param) {
        return Response.success(roleService.update(param.getId(), param.getRoleName(), param.getRemark(), param.getCodes()));
    }

    @PostMapping("/set-active")
    @AdminRequired(PermissionEnum.CONFIG_MANAGE)
    public Response setActive(@RequestBody @Valid SetActiveRoleParam param) {
        roleService.setActive(param.getId(), param.getActive());
        return Response.success();
    }

    @PostMapping("/delete")
    @AdminRequired(PermissionEnum.CONFIG_MANAGE)
    public Response delete(@RequestParam Long id) {
        roleService.delete(id);
        return Response.success();
    }
}
