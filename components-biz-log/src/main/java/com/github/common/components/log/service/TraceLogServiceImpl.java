package com.github.common.components.log.service;

import com.github.common.components.log.model.TraceLog;
import com.github.common.components.rpc.serializer.GlobalSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 日志实现类 需要将tracelog输出到独立的log文件中
 *
 * @author
 * @date 2018/9/11
 */
@Slf4j
@Component
public class TraceLogServiceImpl implements TraceLogService {
  @Override
  public void saveTraceLog(TraceLog traceLog) {
    String loggedStr = GlobalSerializer.serialize(traceLog);
    log.info(loggedStr);
  }
}
