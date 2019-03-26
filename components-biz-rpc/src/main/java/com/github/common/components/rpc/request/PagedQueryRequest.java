package com.github.common.components.rpc.request;

import com.github.common.components.rpc.page.Page;
import lombok.Data;

/**
 * 查询请求
 *
 * @date 2018/7/18
 */
@Data
public class PagedQueryRequest extends CommonRequest {
    /**
     * 分页信息
     */
    private Page page;
}