package com.github.common.components.rpc.response;

import com.github.common.components.util.enums.CodedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 响应码. 不为0的为失败.项目中的响应码应继承该类
 */
@RequiredArgsConstructor
@Getter
public enum RespCodeEnum implements CodedEnum<Byte> {
    FAILURE((byte) -1, "失败"), SUCCESS((byte) 0, "成功");

    private final Byte code;
    private final String desc;

}