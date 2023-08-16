package com.tl.tgGame.callback.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class TxDTO {
    private Long id;

    /**
     * 交易hash
     */
    private String hash;

    /**
     * from
     */
    @TableField("`from`")
    private String from;

    /**
     * to
     */
    @TableField("`to`")
    private String to;

    /**
     * 区块
     */
    private Long block;


    private LocalDateTime createTime;
    /**
     * value
     */
    private BigInteger value;
}
