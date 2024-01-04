package com.tl.tgGame.admin.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@TableName("role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private static final String SEP = ",";

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 激活，是否可用
     */
    private Boolean active;

    /**
     * 角色权限代码
     */
    @TableField("permission_code")
    @JsonIgnore
    private String permissionCode;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Long createdTime;

    @TableField(exist = false)
    private List<String> permissionList;


    public List<String> getCodes() {
        return StringUtils.isNotBlank(this.permissionCode) ? Arrays.asList(this.permissionCode.split(SEP)) : Collections.emptyList();
    }

    public List<String> getPermissionList() {
        List<String> permissionList = new ArrayList<>();
        List<String> codes = getCodes();
        if(CollectionUtils.isEmpty(codes)){
            return permissionList;
        }
        permissionList = codes.stream().map(PermissionEnum::valueOf).map(PermissionEnum::getPathName).collect(Collectors.toList());
        return permissionList;
    }

    public void setCodes(List<String> codes) {
        this.permissionCode = String.join(SEP, codes);
    }
}
