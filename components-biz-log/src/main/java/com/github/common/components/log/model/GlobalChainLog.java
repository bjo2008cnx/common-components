package com.github.common.components.log.model;

import lombok.Data;

import java.io.Serializable;

/** 通用日志对象类 */
@Data
public class GlobalChainLog implements Serializable {

  /** 全局标识 */
  private String globalTraceId;

  /** parentRpcId */
  private String parentRpcId;

  /** RpcId */
  private String rpcId;

  /** Rpc类型 */
  private String rpcType;
}
