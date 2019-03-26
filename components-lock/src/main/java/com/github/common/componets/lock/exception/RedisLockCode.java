package com.github.common.componets.lock.exception;

import com.github.common.components.util.enums.CodedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: luffy
 * @create: 2018-10-20 14:41
 **/
@Getter
@AllArgsConstructor
public enum RedisLockCode implements CodedEnum<String> {

  REDIS_LOCK_ERROR("RL50000","redis锁异常"),
  REDIS_LOCK_KEY_ERROR("RL50001","锁定redis key异常"),
  ;
  private String code;
  private String desc;
}
