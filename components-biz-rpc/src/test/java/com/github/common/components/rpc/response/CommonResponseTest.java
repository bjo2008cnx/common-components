package com.github.common.components.rpc.response;

import com.github.common.components.rpc.serializer.GlobalSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2018/9/11.
 */
public class CommonResponseTest {
    @Test
    public void getHeader() throws Exception {
        ResponseHeader header = ResponseHeader.builder().code(0).message("success").chainId("X0101").build();
        CommonResponse<List> response = new CommonResponse<>(header, Arrays.asList(1, 2));
        String result = GlobalSerializer.serialize(response);

        System.out.println(result);

        CommonResponse commonResponse = GlobalSerializer.parseObject(result, CommonResponse.class);
        Assert.assertEquals(0,commonResponse.getHeader().getCode());
        Assert.assertEquals("success",commonResponse.getHeader().getMessage());
        Assert.assertEquals("X0101",commonResponse.getHeader().getChainId());
    }
}