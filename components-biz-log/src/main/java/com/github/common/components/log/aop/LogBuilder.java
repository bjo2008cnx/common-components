package com.github.common.components.log.aop;

import com.github.common.components.log.model.TraceLog;
import com.github.common.components.rpc.response.CommonResponse;
import com.github.common.components.rpc.serializer.GlobalSerializer;
import com.github.common.components.util.constant.GlobalConstant;
import com.github.common.components.util.lang.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Log构造器
 *
 * @author
 * @date 2018/9/11
 */
@Slf4j
public class LogBuilder {
  /** 异常输出行数 */
  public static final int ROWS = 20;

  /**
   * 构建log对象
   *
   * @param clazz
   * @param method
   * @param inputs
   * @param startTime
   * @param output
   * @param t
   * @return
   */
  public static TraceLog buildLog(
      String clazz, String method, Object[] inputs, Long startTime, Object output, Throwable t) {
    TraceLog.TraceLogBuilder builder = TraceLog.builder();
    try {
      builder.timestamp(new Date(startTime)).timeSpent(System.currentTimeMillis() - startTime);

      builder.providerHost(getIp()).providerAppId(""); // TODO
      builder.className(clazz).method(method);
      builder.concurrent(ConcurrentCounter.getConcurrent(clazz, method).get());
      String outputJson = GlobalSerializer.serialize(output);
      builder.input(GlobalSerializer.serialize(inputs)).output(outputJson);

      // 设置返回结果
      builder.exception(ExceptionUtil.getStackTrace(t, ROWS));
      builder.isSuccess(
          t == null ? GlobalConstant.Booleans.TRUE_INT : GlobalConstant.Booleans.FALSE_INT);
      buildReturnResult(builder, outputJson);
    } catch (Throwable e) {
      log.error("fail to write log:", e);
    }
    return builder.build();
  }

  private static void buildReturnResult(TraceLog.TraceLogBuilder builder, String outputJson) {
    // 判断outputJson是否为正常json
    if (!outputJson.startsWith("{")) {
      return;
    }
    CommonResponse response = GlobalSerializer.parseObject(outputJson, CommonResponse.class);
    builder.returnCode(String.valueOf(response.getHeader().getCode()));
    builder.returnMessage(String.valueOf(response.getHeader().getMessage()));
  }

  private static String getIp() {
    String localIp = null;
    try {
      localIp = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      log.error("fail to get ip", e);
    }
    return localIp;
  }
}
