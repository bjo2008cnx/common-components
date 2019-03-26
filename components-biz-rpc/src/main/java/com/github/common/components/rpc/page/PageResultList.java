package com.github.common.components.rpc.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 带分页的列表
 *
 * @date 2018/7/18
 */
@Data
public class PageResultList<T> implements Serializable {
    private PageResult resultPage;

    private List<T> data;
}