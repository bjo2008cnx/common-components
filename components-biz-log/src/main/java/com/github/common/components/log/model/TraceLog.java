package com.github.common.components.log.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/** 通用日志对象类 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceLog implements Serializable {

  /** 全局标识 */
  private String globalTraceId;

  /** 是否成功:1 成功,0 失败 */
  private int isSuccess = 1;

  /** 服务提供方应appId */
  private String providerAppId;
  /** 服务提供方IP */
  private String providerHost;

  /** 接口名称 */
  private String className;
  /** 接口方法 */
  private String method;

  /** 耗时 */
  private Long timeSpent;

  /** 当前并发量 */
  private Integer concurrent;

  /** 日志业务数据 */
  private String bizData;

  /** 输入 */
  private Object input;

  /** 输出 */
  private Object output;

  /** 调用时间 */
  private Date timestamp;

  /** 异常 */
  private String exception;

  /** 是否保存日志 */
  private Boolean isSaveLog = false;

  /** 错误码 */
  private String returnCode;

  /** 错误信息 */
  private String returnMessage;
}
