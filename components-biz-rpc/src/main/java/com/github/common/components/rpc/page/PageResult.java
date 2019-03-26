package com.github.common.components.rpc.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 分页结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PageResult extends Page {
    /**
     * 总数量
     */
    private int totalCount;

    /**
     * 总页数
     */
    private int totalPage;

}
