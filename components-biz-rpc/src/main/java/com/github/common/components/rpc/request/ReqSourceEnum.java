package com.github.common.components.rpc.request;

import com.github.common.components.util.enums.CodedEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 请求来源
 */
@RequiredArgsConstructor
@Getter
public enum ReqSourceEnum implements CodedEnum<Byte> {
    Andriod((byte) 0, "Andriod"), IOS((byte) 1, "IOS"), H5((byte) 2, "H5"), PC((byte) 3, "PC");

    private final Byte code;
    private final String desc;

}