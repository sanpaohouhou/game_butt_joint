package com.tl.tgGame.common.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PageObject<T> {
    private Long total;
    private Long page;
    private Long size;
    private List<T> items;

    public static <T> PageObject<T> of(IPage<T> iPage) {
        return PageObject.<T>builder()
                .page(iPage.getCurrent())
                .size(iPage.getSize())
                .total(iPage.getTotal())
                .items(iPage.getRecords())
                .build();
    }
}
