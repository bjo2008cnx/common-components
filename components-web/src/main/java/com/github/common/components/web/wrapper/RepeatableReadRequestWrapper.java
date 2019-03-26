package com.github.common.components.web.wrapper;

import com.github.common.components.util.io.StreamUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 可重复读取request
 */
@Slf4j
public class RepeatableReadRequestWrapper extends HttpServletRequestWrapper {

  /**
   * input stream 的buffer
   */
  @Getter
  @Setter
  private final String body;

  /**
   * 构造器:对request进行缓冲
   *
   * @param request {@link javax.servlet.http.HttpServletRequest} object.
   */
  public RepeatableReadRequestWrapper(HttpServletRequest request) {
    super(request);

    String content = null;
    try {
      content = StreamUtil.read(request.getInputStream());
    } catch (IOException e) {
      log.error("fail to read request stream", e);
    }
    body = content;
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    final ByteArrayInputStream stream = new ByteArrayInputStream(body.getBytes());
    return new BufferedServletInputStream(stream);
  }
}
