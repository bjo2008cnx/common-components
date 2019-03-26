package com.github.common.components.web.repeatcheck.handler;

import com.github.common.components.web.repeatcheck.aop.Token;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理类
 *
 * @author
 * @date 2018/9/28
 */
public interface TokenHandler {

  /**
   * 前处理
   */
  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Token token);

  /**
   * 后处理
   */
  void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, Token token);

  void resetToken(HttpServletRequest request, String tokenId);

  /**
   * 判断昌否重复提交
   *
   * @param request reques
   * @return 是否重复提交
   */
  boolean isRepeatSubmit(HttpServletRequest request);

  /**
   * 生成token
   *
   * @param request request
   * @param amount 需要生成的token 数量
   */
  String[] generateToken(HttpServletRequest request, int amount);

  /**
   * 删除token
   *
   * @param request request
   */
  void removeToken(HttpServletRequest request);

  boolean lockToken(HttpServletRequest request, String tokenKey);

}