/**
 *
 */
package com.github.common.components.rpc.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 分页信息，
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page implements Serializable {

    /**
     * 分页大小
     */
    private int pageSize = 20;

    /**
     * 当前页
     */
    private int pageIndex = 1;

    /**
     * order by column
     */
    private String sortName;

    /**
     * ASC or DESC
     */
    private String sortOrder;

}
