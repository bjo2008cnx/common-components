package com.github.common.components.auth.client;

import lombok.Builder;
import lombok.Data;

/**
 * 请求上下文
 *
 * @date 2018/8/11
 */
@Data
@Builder
public class RequestContext {
  /** appId */
  private String appId;

  /** sign */
  private String sign;

  /** timestamp */
  private String timestamp;
}
