package com.tl.tgGame.wallet.dto;

import lombok.Data;

@Data
public class PageQueryParams {

    private Integer pageNumber = 1;

    private Integer pageSize = 20;
}
