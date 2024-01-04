package com.tl.tgGame.admin.role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员
 * </p>
 *
 * @author baomidou
 * @since 2022-07-12
 */
@Data
@Builder
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 备注
     */
    private String note;

    private Boolean enable;

    /**
     * 角色id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
