package com.tl.tgGame.admin.dto;

import com.tl.tgGame.project.enums.Network;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ChargeDTO {
    @NotNull
    private Long userId;
    @DecimalMin("1")
    private BigDecimal amount;
    private String address;
    private Network network;
    private String screen;
    private String note;
    private String hash;
}
