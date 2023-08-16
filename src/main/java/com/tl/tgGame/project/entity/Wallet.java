package com.tl.tgGame.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    private String ethAddress;
    private String tronAddress;
    private String password;
    private String ethPriKey;
    private String tronPriKey;
}
