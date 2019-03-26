package com.github.common.components.buid.worker;


import com.github.common.components.buid.utils.NetUtils;

/**
 * @author Michael.Wang
 * @date 2017/4/27
 */
public class LocalWorkIdAssigner implements WorkerIdAssigner {
    @Override
    public long assignWorkerId() {
        return Long.parseLong(NetUtils.getLocalAddressParts());
    }
}
