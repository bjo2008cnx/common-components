package com.github.common.components.util.base;

import com.github.common.components.util.collection.MapReadUtil;
import com.github.common.components.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/** 对象拷贝工具,支持驼峰风格 */
@Slf4j
public class PopulateUtil {

  /**
   * Properties 转成Object, 属性名转换为驼峰风格
   *
   * @param properties
   * @return
   * @throws Exception
   */
  public static <T> T map2Obj(Properties properties, Class<T> clazz) {
    Map<String, Object> map = PropertiesToMap(properties);
    return map2Obj(map, true, null, clazz);
  }

  /**
   * Properties 转成Object, 属性名转换为驼峰风格
   *
   * @param properties
   * @return
   * @throws Exception
   */
  public static <T> T map2Obj(Properties properties, String prefix, Class<T> clazz) {
    Map<String, Object> map = PropertiesToMap(properties);
    return map2Obj(map, true, prefix, clazz);
  }

  private static Map<String, Object> PropertiesToMap(Properties properties) {
    Map<String, Object> map = new HashMap();
    Set<Map.Entry<Object, Object>> entries = properties.entrySet();
    for (Map.Entry<Object, Object> entry : entries) {
      map.put(entry.getKey().toString(), entry.getValue());
    }
    return map;
  }

  /**
   * Map 转成Object, 属性名转换为驼峰风格
   *
   * @param map
   * @return
   * @throws Exception
   */
  public static <T> T map2Obj(Map<String, Object> map, Class<T> clazz) {
    return map2Obj(map, true, null, clazz);
  }

  /**
   * Map 转成Object, 属性名转换为驼峰风格
   *
   * @param map
   * @return
   * @throws Exception
   */
  public static <T> T map2Obj(Map<String, Object> map, String prefix, Class<T> clazz) {
    return map2Obj(map, true, prefix, clazz);
  }

  /**
   * Map 转成Object, 属性名不转换
   *
   * @param map
   * @return
   * @throws Exception
   */
  public static <T> T map2Obj(
      Map<String, Object> map, boolean convertToCamel, String prefix, Class<T> clazz) {

    T obj;
    try {
      obj = clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    Field[] declaredFields = clazz.getDeclaredFields();
    for (Field field : declaredFields) {
      int mod = field.getModifiers();
      if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
        continue;
      }
      writeField(map, convertToCamel, obj, field, prefix);
    }
    return obj;
  }

  /**
   * 对象转成Map
   *
   * @param obj
   * @return
   * @throws Exception
   */
  public static Map<String, Object> Obj2Map(Object obj) {
    Map<String, Object> map = new HashMap();
    Field[] fields = obj.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        map.put(field.getName(), field.get(obj));
      } catch (IllegalAccessException e) {
        log.warn("fail to convert {}", field.getName(), e);
      }
    }
    return map;
  }

  /**
   * 给field赋值，主要解决驼峰风格问题和类型转换问题
   *
   * @param <T>
   * @param map
   * @param convertToCamel
   * @param obj
   * @param field
   * @param prefix
   */
  public static <T> void writeField(
      Map<String, Object> map, boolean convertToCamel, T obj, Field field, String prefix) {
    field.setAccessible(true);
    try {
      String fieldName = field.getName();
      // 如果是驼峰风格，转成下画线
      String propName = convertToCamel ? StringUtil.camelToUnderLine(fieldName) : fieldName;
      // 如果有前缀，加前缀。如：类中的field 为paassword, map中的key为: jdbc:password
      propName = StringUtil.isNotEmpty(prefix) ? prefix + propName : propName;
      Object value = MapReadUtil.get(map, propName, field.getType());
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      log.error("fail to set field", e);
    }
    field.setAccessible(false);
  }
}
