package com.test.web.dto;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;

@Getter
@Setter
public class PageInfoReq {

    /**
     * @Fields pageNum : 页码
     */
    @Min(value = 0)
    private long pageNum;

    /**
     * @Fields pageSize : 页大小
     */
    @Min(value = 0)
    private long pageSize;

}
