package com.github.components.cache.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Order
 *
 * @date 2018/7/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
  private long id;
  private Date timestamp;
}
