package com.github.common.components.web.repeatcheck.constant;

/**
 * 常量类
 */
public class TokenConstant {

  /**
   * 是否删除token，用于具体业务出现异常不需要删除token时向postHandle传递信息
   */
  public static final String IS_REMOVE_TOKEN = "is_remove_token";

  /**
   * 是否重复提交。Controller中应判断该属性是否为Boolean.TRUE
   */
  public static final String IS_REPEATED_SUBMISSION = "is_repeated_submission";

  /**
   * form 提交时token 的名称
   */
  public static final String REQUEST_KEY_PREFIX = "token";
  /**
   * form初始化时token的前缀。例：第一个token的key为SESSION_ID_PREFIX+"1"(即token_1)
   */
  public static final String TOKEN_ID_PREFIX = "token_";
  /**
   * 是否重复提交，客户端需要根据此标记进行处理
   */
  public static final String REPEAT_SUBMIT_ERROR_MESSAGE = "repeat_submit_error_message";
  /**
   * session过期时间
   */
  public static final int SESSION_EXPIRE_MINUTES = 30; //XXX 提取参数，与session保持一致
}
