package com.github.common.components.util.constant;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 全局常量
 *
 * @author Wangxm
 * @date 2016/5/7
 */
public interface GlobalConstant {

    interface Defaults {
        String DEFAULT_ENCODING = CharSets.UTF8;
    }

    /**
     * 字符集
     */
    interface CharSets {
        String UTF8 = "UTF8";
        String GBK = "GBK";
        String GB2312 = "GB2312";
        String ASCII = "US-ASCII";
        String UTF16 = "UTF-16";
        String UNICODE = "UNICODE";
        String ISO88591 = "ISO-8859-1";

    }

    /**
     * 默认空集合
     */
    interface Collections {
        List EMPTY_LIST = Collections.EMPTY_LIST;
        Map EMPTY_MAP = Collections.EMPTY_MAP;
        Set EMPTY_SET = Collections.EMPTY_SET;
        Properties EMPTY_PROPERTIES = new Properties();
    }

    /**
     * 默认异常
     */
    interface Exceptions {
        RuntimeException TODO_EXCEPTION = new UnsupportedOperationException();
    }

    /**
     * 默认布尔型
     */
    interface Booleans {
        int TRUE_INT = 1;
        int FALSE_INT = 0;
        String TRUE_STR = String.valueOf(TRUE_INT);
        String FALSE_STR = String.valueOf(FALSE_INT);
        String TRUE_CHAR = "true";
    }
}
