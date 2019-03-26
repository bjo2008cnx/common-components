package com.github.common.components.cache.client.exception;

public class RedisException extends RuntimeException {

  public RedisException(String message, Throwable e) {
    super(message, e);
  }

  public RedisException(String message) {
    super(message);
  }
}
