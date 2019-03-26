package com.github.common.components.auth.common;

import com.github.common.components.rpc.request.CommonRequest;
import com.github.common.components.rpc.request.RequestHeader;
import com.github.common.components.util.lang.AssertUtil;
import com.github.common.components.util.lang.Md5Util;
import com.github.common.components.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/** 签名工具类 */
@Slf4j
public class WebSignUtil {
  /**
   * 签名
   *
   * @param request
   * @param secret
   * @return
   */
  public static String sign(CommonRequest request, String secret) {
    RequestHeader header = request.getHeader();
    return sign(header.getAppId(), secret, null, String.valueOf(header.getTimestamp()));
  }

  /**
   * 计算签名
   *
   * @param secret
   * @param params
   * @param timestamp
   * @return
   */
  public static String sign(String appId, String secret, Map params, String timestamp) {
    String text = String.valueOf(timestamp);
    String joinedParam = StringUtil.join('|', appId, text);
    String digest = Md5Util.getMD5StringWithSalt(joinedParam, secret);
    return digest;
  }

  /**
   * @param timestamp
   * @param expireSeconds sign过期时间,单位为秒
   * @return
   */
  public static boolean isExpired(String timestamp, int expireSeconds) {
    try {
      AssertUtil.assertNotEmpty(timestamp, "timestamp");
      long currentTime = System.currentTimeMillis();
      long inTime = Long.parseLong(timestamp);
      return currentTime - inTime >= expireSeconds * 1000;
    } catch (Exception e) {
      log.warn("", e);
      return false;
    }
  }
}
