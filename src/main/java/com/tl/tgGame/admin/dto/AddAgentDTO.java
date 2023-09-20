package com.tl.tgGame.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AddAgentDTO {

    @NotBlank(message = "请输入合伙人名称")
    private String agentName;

    private String mobile;

    /**
     * 商定分红
     */
    @NotNull(message = "请输入商定分红占比")
    private BigDecimal dividendRate;
    /**
     * 备注
     */
    private String remark;

    @NotNull(message = "请输入游戏账号")
    private String gameAccount;

    @NotBlank(message = "请输入用户名")
    private String userName;

    private String password;

    private Long id;
}
