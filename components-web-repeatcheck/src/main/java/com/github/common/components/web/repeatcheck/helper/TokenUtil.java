package com.github.common.components.web.repeatcheck.helper;


import com.github.common.components.rpc.serializer.GlobalSerializer;
import com.github.common.components.util.collection.MapReadUtil;
import com.github.common.components.util.io.StreamUtil;
import com.github.common.components.util.lang.RandomUtil;
import com.github.common.components.web.repeatcheck.constant.TokenConstant;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * token工具
 */
@Slf4j
public class TokenUtil {

  public static final String ENCODING = "utf-8";

  /**
   * 生成Token key
   *
   * @return 生成的token key
   */
  public static String generateTokenKey() {
    return System.currentTimeMillis() + RandomUtil.randomStr(10);
  }

  /**
   * 生成Token key
   *
   * @return 生成的token key
   */
  public static String[] generateTokenKeys(int count) {
    String[] tokenKeys = new String[count];
    for (int i = 0; i < count; i++) {
      tokenKeys[i] = generateTokenKey();
    }
    return tokenKeys;
  }

  /**
   * 判断是否重复提交
   */
  public static boolean isRepeatedSubmission(HttpServletRequest request) {
    return Boolean.TRUE.equals(request.getAttribute(TokenConstant.IS_REPEATED_SUBMISSION));
  }

  /**
   * 增加删除token标记
   */
  public static void addRemoveTokenFlag(HttpServletRequest request) {
    request.setAttribute(TokenConstant.IS_REMOVE_TOKEN, Boolean.TRUE);
  }

  /**
   * 解析token的key,先用getParameter方式获取(form方式)，如果未取到，则从json中获取(ajax方式)
   */
  public static String parseToken(HttpServletRequest request) {
    String tokenKey = request.getParameter(TokenConstant.REQUEST_KEY_PREFIX); //从request中获取tokenKey
    log.debug("Token Key is:" + tokenKey);

    if (tokenKey != null) {
      return tokenKey;
    }
    log.info("Getting Token key by  getParameter fails; try to get token key from input stream ");
    try {
      ServletInputStream in = request.getInputStream();
      if (in.markSupported()) {
        //加标记以便reset
        in.mark(0);
        tokenKey = parseTokenJson(tokenKey, in);
        //reset以便重复读
        in.reset();
      } else {
        log.warn("input stream's mark support is FALSE");
        tokenKey = parseTokenJson(tokenKey, in);
      }
    } catch (IOException e) {
      log.error("fail to parse json", e);
    }
    log.info("Token key after parseToken is : ", tokenKey);
    return tokenKey;
  }

  private static String parseTokenJson(String tokenKey, ServletInputStream inputStream) throws IOException {
    String requestJson = StreamUtil.read(inputStream);
    log.debug("request json is :: " + requestJson);
    Map properties = GlobalSerializer.parseObject(requestJson, Map.class);
    if (log.isDebugEnabled()) {
      log.debug(MapReadUtil.toString(properties));
    }
    tokenKey = (String) properties.get(TokenConstant.REQUEST_KEY_PREFIX);
    return tokenKey;
  }
}