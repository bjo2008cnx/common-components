package com.github.common.components.web.filter;

import com.github.common.components.web.wrapper.RepeatableReadRequestWrapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 可重复读流过滤器
 */
@Slf4j
public class RepeatableReadFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // Do nothing
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    log.info("RepeatableReadFilter [start]");
    try {
      if (request instanceof HttpServletRequest) {
        log.info("request is HttpServletRequest");
        HttpServletRequest req = (HttpServletRequest) request;
        if (!(request instanceof RepeatableReadRequestWrapper)) {
          request = new RepeatableReadRequestWrapper(req);
        }
      }
    } catch (Throwable e) {
      log.error("fail to filter in RepeatableReadFilter", e);
    }
    log.info("RepeatableReadFilter [end]");
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    // Do nothing
  }
}
