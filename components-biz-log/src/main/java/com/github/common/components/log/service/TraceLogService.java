package com.github.common.components.log.service;

import com.github.common.components.log.model.TraceLog;

public interface TraceLogService {

  /**
   * 保存通用日志模型
   *
   * @param traceLog
   */
  void saveTraceLog(TraceLog traceLog);
}
