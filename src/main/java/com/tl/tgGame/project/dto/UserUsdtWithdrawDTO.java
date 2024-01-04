package com.tl.tgGame.project.dto;

import com.tl.tgGame.project.enums.Network;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UserUsdtWithdrawDTO {
    @NotNull
    private Network network;

    @NotBlank
    private String to;

    @DecimalMin("10")
    private BigDecimal amount;
}
