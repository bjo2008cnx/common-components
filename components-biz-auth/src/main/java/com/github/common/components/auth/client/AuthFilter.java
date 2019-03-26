package com.github.common.components.auth.client;

import com.github.common.components.auth.common.WebSignUtil;
import com.github.common.components.rpc.request.CommonRequest;
import com.github.common.components.rpc.request.RequestHeader;
import com.github.common.components.rpc.serializer.JsonUtil;
import com.github.common.components.web.wrapper.RepeatableReadRequestWrapper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * 签名认证过滤器
 *
 * @date 2018/8/15
 */
@Slf4j
public class AuthFilter implements Filter {
  @Value("${common_auth_secret}")
  private String secret;

  /**
   * 解析 RepeatableReadWrapper 封装的body
   *
   * @param req
   * @return
   */
  private static CommonRequest parseWrapperBody(ServletRequest req) {
    CommonRequest obj = null;
    try {
      if (req instanceof RepeatableReadRequestWrapper) {
        String requestBody = ((RepeatableReadRequestWrapper) req).getBody();
        obj = JsonUtil.parseObject(requestBody, CommonRequest.class);
      }
    } catch (Throwable e) {
      log.error("fail to parse body", e);
    }
    return obj;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // do nothing
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    CommonRequest commonRequest = parseWrapperBody(request);
    RequestHeader header = commonRequest.getHeader();
    String signCalculated = WebSignUtil.sign(commonRequest, this.secret);
    if (!signCalculated.equals(header.getSign())) {
      response.getWriter().write("请提供有效签名");
    } else {
      filterChain.doFilter(request, response);
    }
  }

  @Override
  public void destroy() {
    // do nothing
  }
}
