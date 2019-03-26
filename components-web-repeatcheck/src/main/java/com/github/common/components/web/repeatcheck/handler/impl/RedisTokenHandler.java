package com.github.common.components.web.repeatcheck.handler.impl;

import com.github.common.components.cache.client.CacheClient;
import com.github.common.components.util.lang.StringUtil;
import com.github.common.components.web.repeatcheck.constant.TokenConstant;
import com.github.common.components.web.repeatcheck.constant.TokenStatusEnum;
import com.github.common.components.web.repeatcheck.helper.TokenUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * token 拦截器，token保存在redis中
 */
@Slf4j
@Component
public class RedisTokenHandler extends AbstractTokenHandler {

  //TODO
  private CacheClient cacheClient = null;

  /**
   * 生成token 并放入session
   *
   * @param request request
   * @param amount 需要生成的token 数量
   */
  @Override
  public String[] generateToken(HttpServletRequest request, int amount) {
    log.info("Start to call [generateToken]. Parameter amount is :{}", amount);
    //生成tokens
    String[] tokenKeys = TokenUtil.generateTokenKeys(amount);

    for (int i = 0; i < amount; i++) {
      cacheClient.set(tokenKeys[i], TokenStatusEnum.INIT.getKey());
      cacheClient.expire(tokenKeys[i], TokenConstant.SESSION_EXPIRE_MINUTES * 60);
    }
    log.info("Succeed to call [generateToken]");
    return tokenKeys;
  }

  /**
   * 删除token
   *
   * @param request request
   */
  @Override
  public void removeToken(HttpServletRequest request) {
    String tokenKey = TokenUtil.parseToken(request); //从request中获取tokenKey
    try {
      cacheClient.del(tokenKey);
    } catch (Exception e) {
      log.warn("fail to del redis key: {}", tokenKey);
    }
    log.info("Succeed to del token:{} from session:{}", tokenKey);
  }

  @Override
  public boolean lockToken(HttpServletRequest request, String tokenId) {
    if (StringUtil.isEmpty(tokenId)) {
      return false;
    }
    if (cacheClient.exists(tokenId)) {
      cacheClient.set(tokenId, TokenStatusEnum.IN_PROGRESS.getKey());
      return true;
    }
    return false;
  }

  @Override
  public void resetToken(HttpServletRequest request, String tokenId) {
    if (cacheClient.exists(tokenId)) {
      cacheClient.set(tokenId, TokenStatusEnum.INIT.getKey());
    }
  }


  /**
   * 判断是否重复提交
   *
   * @param request request
   */
  @Override
  public boolean isRepeatSubmit(HttpServletRequest request) {
    String tokenKey = TokenUtil.parseToken(request);//从request中获取tokenKeyk
    if (tokenKey == null) {
      log.debug("tokenKey is null");
      //如果client 端的token为空，则认为是重复提交,不允许提交
      return true;
    }

    String tokenValue = cacheClient.get(tokenKey);
    log.debug("token value from redis is :{}. Token isRepeatSubmit:{}", tokenValue, !(TokenStatusEnum.INIT.getKey().equals(tokenValue)));
    //判断redis中token的值，如果不是INIT，则是重复提交
    return !TokenStatusEnum.INIT.getKey().equals(tokenValue);
  }
}