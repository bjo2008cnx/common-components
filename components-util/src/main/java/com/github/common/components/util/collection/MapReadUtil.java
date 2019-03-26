package com.github.common.components.util.collection;

import com.github.common.components.util.constant.GlobalConstant;
import com.github.common.components.util.lang.StringUtil;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Map 读取工具类
 */
public class MapReadUtil {

    /**
     * 获取属性
     *
     * @param map
     * @param attr
     * @param <T>
     * @return
     */
    public static <T> T get(Map map, String attr) {
        return (T) (map.get(attr));
    }

    /**
     * 获取属性，给出默认值
     *
     * @param map
     * @param attr
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T get(Map map, String attr, Object defaultValue) {
        Object result = map.get(attr);
        return (T) (result != null ? result : defaultValue);
    }


    /**
     * 获取属性
     *
     * @param map
     * @param attr
     * @returnss
     */
    public static Object get(Map map, String attr, Class targetType) {
        switch (targetType.getName()) {
            case "java.lang.String":
                return getStr(map, attr);
            case "int":
                return getInt(map, attr);
            case "java.lang.Integer":
                return getInt(map, attr);
            case "long":
                return getLong(map, attr);
            case "java.lang.Long":
                return getLong(map, attr);
            case "boolean":
                return getBoolean(map, attr);
            case "java.lang.Boolean":
                return getBoolean(map, attr);
            default:
                return get(map, attr);
        }
    }

    public static String getStr(Map map, String attr) {
        Object object = map.get(attr);
        if (object == null) {
            return null;
        }

        if (object instanceof String) {
            return (String) object;
        } else {
            return object.toString();
        }
    }

    public static Integer getInt(Map map, String attr) {
        Object prop = map.get(attr);
        if (prop instanceof Integer) {
            return (Integer) map.get(attr);
        } else if (prop instanceof String) {
            String propStr = (String) prop;
            return StringUtil.isEmpty(propStr) ? 0 : Integer.valueOf(propStr);
        } else if (prop instanceof BigDecimal) {
            BigDecimal propStr = (BigDecimal) prop;
            return propStr.intValue();
        }
        return 0;
    }

    public static Long getLong(Map map, String attr) {

        Object prop = map.get(attr);
        if (prop instanceof Long) {
            return (Long) map.get(attr);
        } else if (prop instanceof String) {
            String propStr = (String) prop;
            return Long.valueOf(propStr);
        } else if (prop instanceof BigDecimal) {
            BigDecimal propStr = (BigDecimal) prop;
            return propStr.longValue();
        } else if (prop instanceof Integer) {
            Integer propStr = (Integer) prop;
            return propStr.longValue();
        } else if (prop instanceof BigInteger) {
            BigInteger propStr = (BigInteger) prop;
            return propStr.longValue();
        }
        return 0L;
    }

    /**
     * Get attribute of mysql type: bit, tinyint(1)
     */
    public static boolean getBoolean(Map map, String attr) {
        Object prop = map.get(attr);
        if (prop instanceof Boolean) {
            return (Boolean) map.get(attr);
        } else if (prop instanceof String) {
            return GlobalConstant.Booleans.TRUE_STR.equals(prop) || GlobalConstant.Booleans.TRUE_CHAR.equals(prop);
        } else if (prop instanceof Integer) {
            return Integer.valueOf(GlobalConstant.Booleans.TRUE_INT).equals(prop);
        }
        return false;
    }

    public static BigInteger getBigInteger(Map map, String attr) {
        return (BigInteger) map.get(attr);
    }

    public static Date getDate(Map map, String attr) {
        return (Date) map.get(attr);
    }

    public static java.sql.Time getTime(Map map, String attr) {
        return (java.sql.Time) map.get(attr);
    }

    public static java.sql.Timestamp getTimestamp(Map map, String attr) {
        return (java.sql.Timestamp) map.get(attr);
    }

    public static Double getDouble(Map map, String attr) {
        return (Double) map.get(attr);
    }

    public static Float getFloat(Map map, String attr) {
        return (Float) map.get(attr);
    }

    public static BigDecimal getBigDecimal(Map map, String attr) {
        return (BigDecimal) map.get(attr);
    }

    /**
     * 将map所有元素打印出来
     *
     * @param map
     * @return
     */
    public static String toString(Map map) {
        StringBuffer buffer = new StringBuffer();
        Set<Entry> set = map.entrySet();
        for (Map.Entry entry : set) {
            buffer.append(entry.getKey() + "::" + entry.getValue());
        }
        return buffer.toString();
    }
}