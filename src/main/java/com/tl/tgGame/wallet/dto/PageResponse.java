package com.tl.tgGame.wallet.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResponse<T> extends Response {
    private Long total;
    private Long page;
    private Long size;
    private Collection<T> items;

    public static <T> PageResponse<T> of(IPage<T> iPage) {
        PageResponse<T> response = new PageResponse<>();
        response.setPage(iPage.getCurrent());
        response.setSize(iPage.getSize());
        response.setTotal(iPage.getTotal());
        response.setItems(iPage.getRecords());
        return response;
    }
}
