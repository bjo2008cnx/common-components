package com.github.common.components.web.repeatcheck.handler.impl;

import com.github.common.components.util.lang.StringUtil;
import com.github.common.components.web.repeatcheck.aop.Token;
import com.github.common.components.web.repeatcheck.constant.TokenConstant;
import com.github.common.components.web.repeatcheck.handler.TokenHandler;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/**
 * token 拦截器
 * 1, 对于表单提交方式的处理步骤
 * 1）在视图初始化时，通过Interceptor,在服务器端生成一个唯一的token，同时将该token加到当前用户的Session域中的Token列表中。
 * 2）然后将Token发送到客户端的form表单中，在Form表单中使用隐藏域来存储这个Token，
 * 3）表单提交的时候连同这个Token一起提交到服务器端，然后在服务器端判断客户端提交上来的Token是否在服务器端的Token列表中，
 * 如果不在，那就是重复提交了，此时服务器端就可以不处理重复提交的表单。如果在，则处理表单提交，处理完后从token列表中清除该token。
 * <p>
 * 2,对于ajax提交方式的处理步骤
 * 实现方式类似于表单提交，区别的地方在于不是将token存放在隐藏域中，而是将token放在提交的data中。
 * <p>
 * 表单中token 的name: token_为前缀+顺序号，从1开始，即：token_1,token_2.....
 */
@Slf4j
public abstract class AbstractTokenHandler implements TokenHandler {

  public static final String IS_TOKEN_LOCKED = "is_token_locked";

  public static final String TOKEN_ERROR = "token_error";
  public static final String TOKEN_MISSING_MSG = "提交的请求应包含token,但token缺失";

  /**
   * 预处理，判断是否重复提交
   */
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Token token) {
    try {
      log.info("Start to preHandle.");
      if (token == null) {
        log.info("Token annotation 为空，略过请求");
        return true;
      }
      boolean needRemoveToken = needRemoveToken(request, token);

      //判断是否删除token
      if (!needRemoveToken) {
        return true;
      }
      if (isRepeatSubmit(request)) {  //如果是，需要在此处判断是否重复提交（删除token的动作延迟到postHandle）
        request.setAttribute(TokenConstant.IS_REPEATED_SUBMISSION, Boolean.TRUE);
      } else {
        String tokenKey = parseTokenKey(request);
        if (StringUtil.isEmpty(tokenKey)) {
          log.error(TOKEN_MISSING_MSG);
          request.setAttribute(TOKEN_ERROR, TOKEN_MISSING_MSG);
        } else {
          boolean isLockTokenSuccess = lockToken(request, tokenKey);
          if (isLockTokenSuccess) {
            request.setAttribute(IS_TOKEN_LOCKED, Boolean.TRUE);
          } else {
            //如果Lock失败（已被其他线程占用）则设置为重复提交
            request.setAttribute(TokenConstant.IS_REPEATED_SUBMISSION, Boolean.TRUE);
          }
        }
      }
    } catch (Exception e) {
      log.error("Fail to execute token-preHandle", e);
    }

    return true;
  }

  /**
   * 判断是否需要删除token
   */
  private boolean needRemoveToken(HttpServletRequest request, Token token) {
    boolean needRemoveToken = (request.getParameter(TokenConstant.REQUEST_KEY_PREFIX) != null);
    //判断是否需要删除tokens
    needRemoveToken = needRemoveToken || (token != null && token.remove());
    return needRemoveToken;
  }

  /**
   * 后处理
   * 对于生成token的场景, 需要将生成的token放到modelAndView中
   * 对于删除token的场景，如果业务方抛出异常，则中止执行删除token；如果给出IS_REMOVE_TOKEN为false,也中止执行删除token,否时，将删除token
   */
  public void postHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView, Token token) {
    try {
      //生成token并放到结果中,如果modelAndView不为空，则放到modelAndView中；否则，放到request.attribute中
      generateAndSetTokens(request, modelAndView, token);

      //处理token：若成功，则删除token；否则，reset token
      handleToken(request, token);
    } catch (Exception e) {
      log.error("Fail to execute token-preHandle", e);
    }
  }

  /**
   * 处理token：若成功，则删除token；否则，reset token
   */
  protected void handleToken(HttpServletRequest request, Token token) {
    //判断是否删除token
    boolean needRemoveToken = needRemoveToken(request, token);
    log.info("Token remove annotation is:{}", needRemoveToken);

    //从业务方传递过来的变量
    boolean isRemoveTokenFromBiz = Boolean.TRUE.equals(request.getAttribute(TokenConstant.IS_REMOVE_TOKEN));
    if (needRemoveToken) {
      if (isRemoveTokenFromBiz) {
        this.removeToken(request);
      } else {
        //如果没有获得正常结束的标记，则将token复位，表单可以重新提交
        String tokenKey = parseTokenKey(request);
        if (Boolean.TRUE.equals(request.getAttribute(IS_TOKEN_LOCKED))) {
          this.resetToken(request, tokenKey);
        }
      }
    }
  }

  /**
   * 生成token并放到结果中,如果modelAndView不为空，则放到modelAndView中；否则，放到request.attribute中
   */
  protected void generateAndSetTokens(HttpServletRequest request, ModelAndView modelAndView, Token token) {
    if (token == null) {
      return;
    }

    //判断是否生成token
    int count = token.generate(); //获取生成token的数量
    if (count > 0) {
      String[] tokens = this.generateToken(request, count);
      if (tokens != null) {
        //目前兼容两种情况，通过ModelAndView方式返回的，将生成的token放到ModelAndView中， 否则，放到request.attribute中
        if (modelAndView != null) {
          for (int i = 0; i < tokens.length; i++) {
            modelAndView.addObject(TokenConstant.TOKEN_ID_PREFIX + (i + 1), tokens[i]);
          }
        } else {
          for (int i = 0; i < tokens.length; i++) {
            request.setAttribute(TokenConstant.TOKEN_ID_PREFIX + (i + 1), tokens[i]);
          }
        }
      }
    }
  }

  private String parseTokenKey(HttpServletRequest request) {
    logRequest(request);
    return request.getParameter(TokenConstant.REQUEST_KEY_PREFIX);
  }

  private void logRequest(HttpServletRequest request) {
    if (log.isDebugEnabled()) {
      Map map = request.getParameterMap();
      Set entries = map.entrySet();
      for (Object entry : entries) {
        if (entry instanceof Map.Entry) {
          Map.Entry reqEntry = (Map.Entry) entry;
          log.debug("request key:{},value:{}", reqEntry.getKey(), reqEntry.getValue());
        }
      }
    }
  }
}
