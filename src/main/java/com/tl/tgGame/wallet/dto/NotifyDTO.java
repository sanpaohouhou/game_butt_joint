package com.tl.tgGame.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.tl.tgGame.project.enums.Network;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class NotifyDTO {

    private NotifyType type;
    private BigDecimal amount;
    private boolean success;
    private BigDecimal fee;

    private Long orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime completeTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    private String fromAddress;
    private String toAddress;
    private Network network;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uid;

    private String hash;

}
