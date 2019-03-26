package com.github.components.cache.aop;

import com.github.common.components.cache.aop.CachedDataType;
import com.github.common.components.cache.aop.annotation.CacheGetter;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * CacheTest
 *
 * @date 2018/7/14
 */
@Service
public class OrderService {

  @CacheGetter(
    key = "'OrderId_'+#orderId",
    expire = 2 * 60 * 60,
    DBIndex = 0,
    force = false,
    dataType = CachedDataType.JSON
  )
  public Order getOrder(Long orderId) {
    System.out.println("find order in db.");
    return new Order(1L, new Date());
  }
}
