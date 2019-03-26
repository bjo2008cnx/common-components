package com.github.common.components.rpc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {
    /**
     * 响应头信息
     */
    private ResponseHeader header;

    /**
     * 响应腰带：需要传递的业务对象
     */
    private T body;

}