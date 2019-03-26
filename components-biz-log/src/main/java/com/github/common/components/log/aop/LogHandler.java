package com.github.common.components.log.aop;

import com.github.common.components.log.model.TraceLog;
import com.github.common.components.log.service.TraceLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * log处理器
 *
 * @author
 * @date 2018/9/11
 */
@Slf4j
@Component
public class LogHandler {

  /** 日志处理类 */
  @Autowired private TraceLogService logService;

  /**
   * 执行前处理
   *
   * @param className
   * @param methodName
   * @return
   */
  public int preHandle(String className, String methodName) {
    return ConcurrentCounter.getConcurrent(className, methodName).incrementAndGet();
  }

  /**
   * 执行后处理
   *
   * @param inputParameters
   * @param className
   * @param methodName
   * @param startTime
   * @param output
   * @param t
   */
  public void postHandle(
      Object[] inputParameters,
      String className,
      String methodName,
      Long startTime,
      Long endTime,
      Object output,
      Throwable t) {
    TraceLog ioLog =
        LogBuilder.buildLog(className, methodName, inputParameters, startTime, output, t);
    try {
      logService.saveTraceLog(ioLog);
    } catch (Exception e) {
      log.error(
          "aop save log error, service:"
              + ioLog.getClassName()
              + ",method=:"
              + ioLog.getMethod()
              + ",input=:"
              + ioLog.getInput(),
          e);
    }
    // 并发计数减值
    ConcurrentCounter.getConcurrent(className, methodName).decrementAndGet();
  }
}
