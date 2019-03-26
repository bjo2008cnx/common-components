package com.github.cat.service;

import com.github.common.monitor.aop.MonitorTransaction;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  @MonitorTransaction
  void delete(Object entity);

  @MonitorTransaction
  void getAllObjects();

  @MonitorTransaction
  void save(Object entity);

  void update(Object entity);
}