package com.github.common.components.util.lang;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些通用的字符串处理
 */
public class StringUtil {

    private static final int INDEX_NOT_FOUND = -1;

    private static final String EMPTY = "";
    /**
     * <p>
     * The maximum size to which the padding constant(s) can expand.
     * </p>
     */
    private static final int PAD_LIMIT = 8192;

    public static final char UNDERLINE = '_';
    private static final String DEFAULT_ENCODING = "UTF-8";

    // 用于驼峰转下划线
    private static Pattern linePattern = Pattern.compile("_(\\w)");
    // 用于驼峰转下划线
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * <p>
     * 只从源字符串中移除指定开头子字符串.
     * </p>
     * <p/>
     * <pre>
     * StringUtil.removeStart(null, *)      = null
     * StringUtil.removeStart("", *)        = ""
     * StringUtil.removeStart(*, null)      = *
     * StringUtil.removeStart("www.dal.com", "www.")   = "dal.com"
     * StringUtil.removeStart("dal.com", "www.")       = "dal.com"
     * StringUtil.removeStart("www.dal.com", "dal") = "www.dal.com"
     * StringUtil.removeStart("abc", "")    = "abc"
     * </pre>
     *
     * @param str      源字符串
     * @param toRemove 将要被移除的子字符串
     * @return String
     */
    public static String removeStart(String str, String toRemove) {
        if (isEmpty(str) || isEmpty(toRemove)) {
            return str;
        }
        if (str.startsWith(toRemove)) {
            return str.substring(toRemove.length());
        }
        return str;
    }

    /**
     * <p>
     * 只从源字符串中移除指定结尾的子字符串.
     * </p>
     * <p/>
     * <pre>
     * StringUtil.removeEnd(null, *)      = null
     * StringUtil.removeEnd("", *)        = ""
     * StringUtil.removeEnd(*, null)      = *
     * StringUtil.removeEnd("www.dal.com", ".com.")  = "www.dal.com"
     * StringUtil.removeEnd("www.dal.com", ".com")   = "www.dal"
     * StringUtil.removeEnd("www.dal.com", "dal") = "www.dal.com"
     * StringUtil.removeEnd("abc", "")    = "abc"
     * </pre>
     *
     * @param str      源字符串
     * @param toRemove 将要被移除的子字符串
     * @return String
     */
    public static String removeEnd(String str, String toRemove) {
        if (isEmpty(str) || isEmpty(toRemove)) {
            return str;
        }
        if (str.endsWith(toRemove)) {
            return str.substring(0, str.length() - toRemove.length());
        }
        return str;
    }

    /**
     * <p>
     * 将一个字符串重复N次
     * </p>
     * <p/>
     * <pre>
     * StringUtil.repeat(null, 2) = null
     * StringUtil.repeat("", 0)   = ""
     * StringUtil.repeat("", 2)   = ""
     * StringUtil.repeat("validator", 3)  = "aaa"
     * StringUtil.repeat("ab", 2) = "abab"
     * StringUtil.repeat("validator", -2) = ""
     * </pre>
     *
     * @param str    源字符串
     * @param repeat 重复的次数
     * @return String
     */
    public static String repeat(String str, int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(str.charAt(0), repeat);
        }

        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                return repeat(str.charAt(0), repeat);
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                StringBuilder buf = new StringBuilder(outputLength);
                for (int i = 0; i < repeat; i++) {
                    buf.append(str);
                }
                return buf.toString();
        }
    }

    /**
     * <p>
     * 将一个字符串重复N次，并且中间加上指定的分隔符
     * </p>
     * <p/>
     * <pre>
     * StringUtil.repeat(null, null, 2) = null
     * StringUtil.repeat(null, "x", 2)  = null
     * StringUtil.repeat("", null, 0)   = ""
     * StringUtil.repeat("", "", 2)     = ""
     * StringUtil.repeat("", "x", 3)    = "xxx"
     * StringUtil.repeat("?", ", ", 3)  = "?, ?, ?"
     * </pre>
     *
     * @param str       源字符串
     * @param separator 分隔符
     * @param repeat    重复次数
     * @return String
     */
    public static String repeat(String str, String separator, int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        } else {
            // given that repeat(String, int) is quite optimized, better to rely
            // on it than try and splice this into it
            String result = repeat(str + separator, repeat);
            return removeEnd(result, separator);
        }
    }

    /**
     * <p>
     * 将某个字符重复N次.
     * </p>
     *
     * @param ch     某个字符
     * @param repeat 重复次数
     * @return String
     */
    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * <p>
     * 扩大字符串长度，从左边补充空格
     * </p>
     * <p/>
     * <pre>
     * StringUtil.leftPad(null, *)   = null
     * StringUtil.leftPad("", 3)     = "   "
     * StringUtil.leftPad("bat", 3)  = "bat"
     * StringUtil.leftPad("bat", 5)  = "  bat"
     * StringUtil.leftPad("bat", 1)  = "bat"
     * StringUtil.leftPad("bat", -1) = "bat"
     * </pre>
     *
     * @param str  源字符串
     * @param size 扩大后的长度
     * @return String
     */
    public static String leftPad(String str, int size) {
        return leftPad(str, size, ' ');
    }

    /**
     * <p>
     * 扩大字符串长度，从左边补充空格
     * </p>
     * <p/>
     * <pre>
     * StringUtil.leftPad(null, *)   = null
     * StringUtil.leftPad("", 3)     = "   "
     * StringUtil.leftPad("bat", 3)  = "bat"
     * StringUtil.leftPad("bat", 5)  = "  bat"
     * StringUtil.leftPad("bat", 1)  = "bat"
     * StringUtil.leftPad("bat", -1) = "bat"
     * </pre>
     *
     * @param number 源字符串
     * @param size   扩大后的长度
     * @return String
     */
    public static String leftPad(int number, int size, char padChar) {
        return leftPad(String.valueOf(number), size, padChar);
    }

    public static String leftPad(long number, int size, char padChar) {
        return leftPad(String.valueOf(number), size, padChar);
    }

    /**
     * <p>
     * 扩大字符串长度，从左边补充指定的字符
     * </p>
     * <p/>
     * <pre>
     * StringUtil.leftPad(null, *, *)     = null
     * StringUtil.leftPad("", 3, 'z')     = "zzz"
     * StringUtil.leftPad("bat", 3, 'z')  = "bat"
     * StringUtil.leftPad("bat", 5, 'z')  = "zzbat"
     * StringUtil.leftPad("bat", 1, 'z')  = "bat"
     * StringUtil.leftPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     源字符串
     * @param size    扩大后的长度
     * @param padChar 补充的字符
     * @return String
     */
    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    /**
     * <p>
     * 扩大字符串长度，从左边补充指定的字符
     * </p>
     * <p/>
     * <pre>
     * StringUtil.leftPad(null, *, *)      = null
     * StringUtil.leftPad("", 3, "z")      = "zzz"
     * StringUtil.leftPad("bat", 3, "yz")  = "bat"
     * StringUtil.leftPad("bat", 5, "yz")  = "yzbat"
     * StringUtil.leftPad("bat", 8, "yz")  = "yzyzybat"
     * StringUtil.leftPad("bat", 1, "yz")  = "bat"
     * StringUtil.leftPad("bat", -1, "yz") = "bat"
     * StringUtil.leftPad("bat", 5, null)  = "  bat"
     * StringUtil.leftPad("bat", 5, "")    = "  bat"
     * </pre>
     *
     * @param str    源字符串
     * @param size   扩大后的长度
     * @param padStr 补充的字符串
     * @return String
     */
    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }


    /**
     * <p>
     * 扩大字符串长度，从左边补充指定字符
     * </p>
     * <p/>
     * <pre>
     * StringUtil.rightPad(null, *, *)      = null
     * StringUtil.rightPad("", 3, "z")      = "zzz"
     * StringUtil.rightPad("bat", 3, "yz")  = "bat"
     * StringUtil.rightPad("bat", 5, "yz")  = "batyz"
     * StringUtil.rightPad("bat", 8, "yz")  = "batyzyzy"
     * StringUtil.rightPad("bat", 1, "yz")  = "bat"
     * StringUtil.rightPad("bat", -1, "yz") = "bat"
     * StringUtil.rightPad("bat", 5, null)  = "bat  "
     * StringUtil.rightPad("bat", 5, "")    = "bat  "
     * </pre>
     *
     * @param str    源字符串
     * @param size   扩大后的长度
     * @param padStr 在右边补充的字符串
     * @return String
     */
    public static String rightPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            char[] padding = new char[pads];
            char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }


    /**
     * <p>
     * 字符串长度达不到指定长度时，在字符串右边补指定的字符.
     * </p>
     * <p/>
     * <pre>
     * StringUtil.rightPad(null, *, *)     = null
     * StringUtil.rightPad("", 3, 'z')     = "zzz"
     * StringUtil.rightPad("bat", 3, 'z')  = "bat"
     * StringUtil.rightPad("bat", 5, 'z')  = "batzz"
     * StringUtil.rightPad("bat", 1, 'z')  = "bat"
     * StringUtil.rightPad("bat", -1, 'z') = "bat"
     * </pre>
     *
     * @param str     源字符串
     * @param size    指定的长度
     * @param padChar 进行补充的字符
     * @return String
     */
    public static String rightPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    /**
     * <p>
     * 截取一个字符串的前几个.
     * </p>
     * <p/>
     * <pre>
     * StringUtil.left(null, *)    = null
     * StringUtil.left(*, -ve)     = ""
     * StringUtil.left("", *)      = ""
     * StringUtil.left("abc", 0)   = ""
     * StringUtil.left("abc", 2)   = "ab"
     * StringUtil.left("abc", 4)   = "abc"
     * </pre>
     *
     * @param str 源字符串
     * @param len 截取的长度
     * @return the String
     */
    public static String left(String str, int len) {
        if (str == null) {
            return null;
        }
        if (len < 0) {
            return EMPTY;
        }
        if (str.length() <= len) {
            return str;
        }
        return str.substring(0, len);
    }

    /**
     * <p>
     * 得到tag字符串中间的子字符串，只返回第一个匹配项。
     * </p>
     * <p/>
     * <pre>
     * StringUtil.substringBetween(null, *)            = null
     * StringUtil.substringBetween("", "")             = ""
     * StringUtil.substringBetween("", "tag")          = null
     * StringUtil.substringBetween("tagabctag", null)  = null
     * StringUtil.substringBetween("tagabctag", "")    = ""
     * StringUtil.substringBetween("tagabctag", "tag") = "abc"
     * </pre>
     *
     * @param str 源字符串。
     * @param tag 标识字符串。
     * @return String 子字符串, 如果没有符合要求的，返回{@code null}。
     */
    public static String substringBetween(String str, String tag) {
        return substringBetween(str, tag, tag);
    }

    /**
     * <p>
     * 得到两个字符串中间的子字符串，只返回第一个匹配项。
     * </p>
     * <p/>
     * <pre>
     * StringUtil.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtil.substringBetween(null, *, *)          = null
     * StringUtil.substringBetween(*, null, *)          = null
     * StringUtil.substringBetween(*, *, null)          = null
     * StringUtil.substringBetween("", "", "")          = ""
     * StringUtil.substringBetween("", "", "]")         = null
     * StringUtil.substringBetween("", "[", "]")        = null
     * StringUtil.substringBetween("yabcz", "", "")     = ""
     * StringUtil.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtil.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str   源字符串
     * @param open  起字符串。
     * @param close 末字符串。
     * @return String 子字符串, 如果没有符合要求的，返回{@code null}。
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * <p>
     * 得到两个字符串中间的子字符串，所有匹配项组合为数组并返回。
     * </p>
     * <p/>
     * <pre>
     * StringUtil.substringsBetween("[validator][b][c]", "[", "]") = ["validator","b","c"]
     * StringUtil.substringsBetween(null, *, *)            = null
     * StringUtil.substringsBetween(*, null, *)            = null
     * StringUtil.substringsBetween(*, *, null)            = null
     * StringUtil.substringsBetween("", "[", "]")          = []
     * </pre>
     *
     * @param str   源字符串
     * @param open  起字符串。
     * @param close 末字符串。
     * @return String 子字符串数组, 如果没有符合要求的，返回{@code null}。
     */
    public static String[] substringsBetween(String str, String open, String close) {
        if (str == null || isEmpty(open) || isEmpty(close)) {
            return null;
        }
        int strLen = str.length();
        if (strLen == 0) {
            return new String[0];
        }
        int closeLen = close.length();
        int openLen = open.length();
        List<String> list = new ArrayList<String>();
        int pos = 0;
        while (pos < strLen - closeLen) {
            int start = str.indexOf(open, pos);
            if (start < 0) {
                break;
            }
            start += openLen;
            int end = str.indexOf(close, start);
            if (end < 0) {
                break;
            }
            list.add(str.substring(start, end));
            pos = end + closeLen;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 切换字符串中的所有字母大小写。<br/>
     * <p/>
     * <pre>
     * StringUtil.swapCase(null)                 = null
     * StringUtil.swapCase("")                   = ""
     * StringUtil.swapCase("The dog has validator BONE") = "tHE DOG HAS A bone"
     * </pre>
     *
     * @param str 源字符串
     * @return String
     */
    public static String swapCase(String str) {
        if (StringUtil.isEmpty(str)) {
            return str;
        }
        char[] buffer = str.toCharArray();

        boolean whitespace = true;

        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isUpperCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
                whitespace = false;
            } else if (Character.isTitleCase(ch)) {
                buffer[i] = Character.toLowerCase(ch);
                whitespace = false;
            } else if (Character.isLowerCase(ch)) {
                if (whitespace) {
                    buffer[i] = Character.toTitleCase(ch);
                    whitespace = false;
                } else {
                    buffer[i] = Character.toUpperCase(ch);
                }
            } else {
                whitespace = Character.isWhitespace(ch);
            }
        }
        return new String(buffer);
    }

    /**
     * 截取出最后一个标志位之后的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     *
     * @param sourceStr 被截取的字符串
     * @param expr      分隔符
     * @return String
     */
    public static String substringAfterLast(String sourceStr, String expr) {
        if (isEmpty(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }

        int pos = sourceStr.lastIndexOf(expr);
        if (pos == -1) {
            return sourceStr;
        }
        return sourceStr.substring(pos + expr.length());
    }

    /**
     * 截取出最后一个标志位之前的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     *
     * @param sourceStr 被截取的字符串
     * @param expr      分隔符
     * @return String
     */
    public static String substringBeforeLast(String sourceStr, String expr) {
        if (isEmpty(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }
        int pos = sourceStr.lastIndexOf(expr);
        if (pos == -1) {
            return sourceStr;
        }
        return sourceStr.substring(0, pos);
    }

    /**
     * 截取出第一个标志位之后的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     *
     * @param sourceStr 被截取的字符串
     * @param expr      分隔符
     * @return String
     */
    public static String substringAfter(String sourceStr, String expr) {
        if (isEmpty(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }

        int pos = sourceStr.indexOf(expr);
        if (pos == -1) {
            return sourceStr;
        }
        return sourceStr.substring(pos + expr.length());
    }

    /**
     * 截取出第一个标志位之前的字符串.<br/>
     * 如果sourceStr为empty或者expr为null，直接返回源字符串。<br/>
     * 如果expr长度为0，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中不存在，直接返回sourceStr。<br/>
     * 如果expr在sourceStr中存在不止一个，以第一个位置为准。
     *
     * @param sourceStr 被截取的字符串
     * @param expr      分隔符
     * @return String
     */
    public static String substringBefore(String sourceStr, String expr) {
        if (isEmpty(sourceStr) || expr == null) {
            return sourceStr;
        }
        if (expr.length() == 0) {
            return sourceStr;
        }
        int pos = sourceStr.indexOf(expr);
        if (pos == -1) {
            return sourceStr;
        }
        return sourceStr.substring(0, pos);
    }

    /**
     * 检查这个字符串是不是空字符串。<br/>
     * 如果这个字符串为null或者trim后为空字符串则返回true，否则返回false。
     *
     * @param str 被检查的字符串
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return isNullOrEmpty(str);
    }

    public static boolean isBlank(String str){
        return isNullOrEmpty(str);
    }

    public static boolean isNotBlank(String str){
        return !isBlank(str);
    }

    /**
     * 检查这个字符串是不是空字符串。<br/>
     * 如果这个字符串为null或者trim后为空字符串则返回true，否则返回false。
     *
     * @param chkStr
     * @return
     */
    public static boolean isNullOrEmpty(String chkStr) {
        if (chkStr == null) {
            return true;
        } else {
            return "".equals(chkStr.trim());
        }
    }

    /**
     * 判断是否除了逗号之外没有都是empty
     *
     * @param str
     * @return
     */
    public static boolean isEmptyExcepComma(String str) {
        if (isEmpty(str)) return true;
        String[] splits = str.split(",");
        for (String split : splits) {
            if (!isEmpty(split)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 如果字符串没有超过最长显示长度返回原字符串，否则从开头截取指定长度并加...返回。
     *
     * @param str    原字符串
     * @param length 字符串最长显示的长度
     * @return 转换后的字符串
     */
    public static String trimString(String str, int length) {
        if (str == null) {
            return "";
        } else if (str.length() > length) {
            return str.substring(0, length - 3) + "...";
        } else {
            return str;
        }
    }

    /**
     * 功能: 把"123,234,567"转为new String[]{"123", "234", "567"}
     *
     * @param str
     * @return
     */
    public static String[] split(String str) {
        return split(str, ",");
    }

    /**
     * 功能: 把"123,234,567"转为new String[]{"123", "234", "567"}
     *
     * @param str
     * @return
     */
    public static Long[] splitToLongArray(String str) {
        return splitToLongArray(str, ",");
    }

    /**
     * 功能: 把"123,234,567"转为new String[]{"123", "234", "567"}
     *
     * @param str
     * @param splitKey
     * @return
     */
    public static String[] split(String str, String splitKey) {
        String[] result = null;
        if (str != null && str.length() > 0) {
            result = str.split(splitKey, -1);
        }
        if (result == null) {
            result = new String[0];
        }
        return result;
    }

    /**
     * 字符串转成数组，并过滤空值
     *
     * @param strs
     * @return
     */
    public static String[] parseToArrayIgnoreEmpty(String strs, String splitKey) {
        if (isEmpty(strs)) {
            return null;
        }
        String[] str1s = strs.split(splitKey);
        StringBuilder strAlls = new StringBuilder();
        for (int i = 0; i < str1s.length; i++) {
            if (isNotEmpty(str1s[i])) {
                strAlls.append(str1s[i]);
                strAlls.append(",");
            }
        }
        if (strAlls.length() > 1) {
            return strAlls.substring(0, strAlls.length() - 1).split(",");
        } else {
            return null;
        }
    }

    /**
     * 功能: 把"123,234,567"转为new Long[]{"123", "234", "567"}
     *
     * @param str
     * @param splitKey
     * @return
     */
    public static Long[] splitToLongArray(String str, String splitKey) {
        Long[] result = null;
        if (str != null && str.length() > 0) {
            String[] strArray = str.split(splitKey, -1);
            result = new Long[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                result[i] = new Long(strArray[i]);
            }
        }
        if (result == null) {
            result = new Long[0];
        }
        return result;
    }


    /**
     * 把指定字符串strSource 中的strFrom 全部替换为strTo,不是正则表达式
     *
     * @param strSource
     * @param strFrom
     * @param strTo
     * @return
     */
    public static String replaceAll(String strSource, String strFrom, String strTo) {
        if (strSource == null) {
            return null;
        }
        if (strFrom == null || strFrom.length() == 0 || strTo == null) {
            return strSource;
        }
        StringBuilder sbDest = new StringBuilder();
        int intPos;
        while ((intPos = strSource.indexOf(strFrom)) > -1) {
            sbDest.append(strSource.substring(0, intPos));
            sbDest.append(strTo);
            strSource = strSource.substring(intPos + strFrom.length());
        }
        sbDest.append(strSource);
        return sbDest.toString();
    }

    /**
     * 把str中的第1个sequence1替换为sequence2
     *
     * @param str
     * @param sequence1 the old character sequence
     * @param sequence2 the new character sequence
     * @return
     */
    public static String replaceFirst(String str, CharSequence sequence1, CharSequence sequence2) {
        if (sequence2 == null) {
            throw new IllegalArgumentException("sequence2 cannot be null");
        }
        int sequence1Length = sequence1.length();
        if (sequence1Length == 0) {
            StringBuilder result = new StringBuilder(str.length() + sequence2.length());
            result.append(sequence2);
            result.append(str);
            return result.toString();
        }
        StringBuilder result = new StringBuilder();
        char first = sequence1.charAt(0);
        int start = 0, copyStart = 0, firstIndex;
        while (start < str.length()) {
            if ((firstIndex = str.indexOf(first, start)) == -1) {
                break;
            }
            boolean found = true;
            if (sequence1.length() > 1) {
                if (firstIndex + sequence1Length > str.length()) {
                    break;
                }
                for (int i = 1; i < sequence1Length; i++) {
                    if (str.charAt(firstIndex + i) != sequence1.charAt(i)) {
                        found = false;
                        break;
                    }
                }
            }
            if (found) {
                result.append(str.substring(copyStart, firstIndex));
                result.append(sequence2);
                copyStart = start = firstIndex + sequence1Length;
                break; // 只发现一次就退出
            } else {
                start = firstIndex + 1;
            }
        }
        if (result.length() == 0 && copyStart == 0) {
            return str;
        }
        result.append(str.substring(copyStart));
        return result.toString();
    }

    /**
     * 显示数据前过滤掉null
     *
     * @param str
     * @return
     */
    public static String filterNull(String str) {
        return str == null ? "" : str;
    }

    public static String filterNull(Object obj) {
        if (obj != null) {
            return filterNull(obj.toString());
        } else {
            return "";
        }
    }

    /**
     * 判断一个数组是否包含一个字符串
     *
     * @param arrayString
     * @param str
     * @return
     */
    public static boolean contains(String str, String... arrayString) {
        if (arrayString == null || arrayString.length == 0) {
            return false;
        }
        for (int i = 0; i < arrayString.length; i++) {
            if (arrayString[i].equals(str)) return true;
        }
        return false;
    }

    /**
     * 功能: 把new String[]{"abc", null, "123"}转化为 "abc,123"
     *
     * @param objects
     * @param splitter
     * @return
     */
    public static String join(char splitter, String... objects) {
        return join(objects, splitter);
    }

    /**
     * 功能: 把new String[]{"abc", null, "123"}转化为 "abc,123"
     *
     * @param objects
     * @param splitter
     * @return
     */
    public static String join(char splitter, Object... objects) {
        return join(objects, splitter);
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param list 需要处理的列表
     * @return 处理后的字符串
     */
    public static String join(Collection list) {
        return join(list, ",");
    }

    /**
     * 功能: 把new long[]{1L, 2L, 3L}转化为 "1,2,3"
     *
     * @param longs
     * @param splitStr
     * @return 如果longs为null, 返回null
     */
    public static String join(long[] longs, String splitStr) {
        StringBuilder sb = new StringBuilder();
        if (longs == null || longs.length == 0) {
            return null;
        }
        for (int i = 0; i < longs.length; i++) {
            if (sb.length() > 0) {
                sb.append(splitStr);
            }
            sb.append(longs[i]);
        }
        return sb.toString();
    }

    /**
     * 将任意对象拼成字符串
     *
     * @param objects
     * @param spliter
     * @return
     */
    public static String join(Object[] objects, char spliter) {
        StringBuilder sb = new StringBuilder();
        if (objects == null || objects.length == 0) {
            return null;
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                continue;
            }
            String obj = objects[i].toString();
            if (obj != null && obj.length() > 0) {
                if (sb.length() > 0) {
                    sb.append(spliter);
                }
                sb.append(obj);
            }
        }
        return sb.toString();
    }


    /**
     * 功能: 得到str的首字母大写
     *
     * @param str
     * @return
     */
    public static String toFirstUpperCase(String str) {
        if (str == null || str.length() == 0) {
            return str;
        } else {
            String firstStr = str.substring(0, 1);
            return firstStr.toUpperCase() + str.substring(1);
        }
    }

    /**
     * 功能: 得到百分比的显示
     *
     * @param numerator
     * @param denominator
     * @return
     */
    public static String getPercentage(int numerator, int denominator) {
        return getPercentage(numerator * 1.00, denominator * 1.00);
    }

    /**
     * 功能: 得到百分比的显示
     *
     * @param numerator
     * @param denominator
     * @return
     */
    public static String getPercentage(double numerator, double denominator) {
        double percentage = numerator * 1.00 / denominator;
        if (String.valueOf(percentage).endsWith(String.valueOf(Double.NaN))) {
            return "";
        }
        percentage = percentage * 100;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(percentage) + "%";
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断是否在范围内,如果equals范围内的任一值，则返回true
     *
     * @param toCompare
     * @param values
     * @return
     */
    public static boolean in(String toCompare, String... values) {
        if (isEmpty(toCompare)) {
            throw new IllegalArgumentException("toCompare is empty");
        }
        for (String value : values) {
            if (toCompare.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是一个中文汉字
     *
     * @param c 字符
     * @return true表示是中文汉字，false表示是英文字母
     * @throws UnsupportedEncodingException 使用了JAVA不支持的编码格式
     */
    public static boolean isChineseChar(char c, String charsetName) {
        // 如果字节数大于1，是汉字
        try {
            return String.valueOf(c).getBytes(charsetName).length > 1;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按字节截取字符串,一个汉字当作一个字符
     *
     * @param orignal 原始字符串
     * @param count   截取位数
     * @return 截取后的字符串
     */
    public static String substring(String orignal, int count, String encoding) {
        int num = count;
        // 原始字符不为null，也不是空字符串
        if (orignal != null && !"".equals(orignal)) {
            // 将原始字符串转换为GBK编码格式
            try {
                orignal = new String(orignal.getBytes(), encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            // 要截取的字节数大于0，且小于原始字符串的字节数
            byte[] bytes;
            try {
                bytes = orignal.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            if (count > 0 && count < bytes.length) {
                StringBuffer buff = new StringBuffer();
                char c;
                for (int i = 0; i < count; i++) {
                    c = orignal.charAt(i);
                    buff.append(c);
                    if (isChineseChar(c, encoding)) {
                        // 遇到中文汉字，截取字节总数减1
                        --count;
                    }
                }
                if (orignal.getBytes().length > num) {
                    orignal += "...";
                }
                return buff.toString() + "...";
            }
        }
        if (orignal.getBytes().length > num) {
            orignal += "...";
        }
        return orignal;
    }

    /**
     * 加单引号
     *
     * @param str
     * @return
     */
    public static String quoteSingle(String str) {
        if (isEmpty(str)) {
            return "''";
        }
        return "'" + str + "'";
    }


    /**
     * 加双引号
     *
     * @param str
     * @return
     */
    public static String quoteDouble(String str) {
        if (isEmpty(str)) {
            return "\"\"";
        }
        return "\"" + str + "\"";
    }


    /**
     * 格式化字符串
     * <p/>
     * %c             字符
     * %d             有符号十进制整数
     * %f              浮点数(包括float和doulbe)
     * %e(%E)     浮点数指数输出[e-(E-)记数法]
     * %g(%G)     浮点数不显无意义的零"0"
     * %i              有符号十进制整数(与%d相同)
     * %u             无符号十进制整数
     * %o             八进制整数    e.g.     0123
     * %x(%X)      十六进制整数0f(0F)   e.g.   0x1234
     * %p             指针
     * %s             字符串
     * %%            "%"
     */
    public static String format(String template, Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template.substring(templateStart, placeholderStart));
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }

    /**
     * 拼接字符
     *
     * @param separator
     * @param toJoin
     * @param mount
     * @return
     */
    public static String join(char separator, char toJoin, int mount) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < mount; i++) {
            buffer.append(toJoin);
            if (i != mount - 1) {
                buffer.append(separator);
            }
        }
        return buffer.toString();
    }

    /**
     * 驼峰风格变成下划线风格
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     *  下划线转化为驼峰风格
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * 判断字符串是否以toCompares中的任一字符串开头
     * 相当于startsWith(str,toComares[0])||startsWith(str,toComares[1])||startsWith(str,toComares[2]).....
     *
     * @param str
     * @param toCompares
     * @return
     */
    public static boolean startsWith(String str, String... toCompares) {
        if (isEmpty(str)) {
            return false;
        }
        for (String toCompare : toCompares) {
            if (str.startsWith(toCompare)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否已toCompares中的任一字符串结尾
     * endsWith(str,toComares[0])||endsWith(str,toComares[1])||endsWith(str,toComares[2])....
     *
     * @param str
     * @param toCompares
     * @return
     */
    public static boolean endsWith(String str, String... toCompares) {
        if (isEmpty(str)) {
            return false;
        }
        for (String toCompare : toCompares) {
            if (str.endsWith(toCompare)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为数字
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = true
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否严格是数字，
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = false
     * </pre>
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isNumericStrict(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 截取字符串左侧的Num位截取到末尾
     *
     * @param str1 处理的字符串
     * @param num  开始位置
     * @return 截取后的字符串
     */
    public static String ltrim(String str1, int num) {
        String tt = "";
        if (!isEmpty(str1) && str1.length() >= num) {
            tt = str1.substring(num, str1.length());
        }
        return tt;

    }

    /**
     * 截取字符串右侧第0位到第Num位
     *
     * @param str 处理的字符串
     * @param num 截取的位置
     * @return 截取后的字符串
     */
    public static String rtrim(String str, int num) {
        if (!isEmpty(str) && str.length() > num) {
            str = str.substring(0, str.length() - num);
        }
        return str;
    }


    /**
     * 截取字符串右侧指定长度的字符串
     *
     * @param input 输入字符串
     * @param count 截取长度
     * @return 截取字符串
     * Summary 其他编码的有待测试
     */
    public static String right(String input, int count) {
        if (isEmpty(input)) {
            return "";
        }
        count = (count > input.length()) ? input.length() : count;
        return input.substring(input.length() - count, input.length());
    }

    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     *
     * @param str 需要处理的字符串1
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 判断字符串数组中是否包含某字符串元素
     *
     * @param substring 某字符串`
     * @param sources   源字符串数组
     * @return 包含则返回true，否则返回false
     */
    public static boolean isIn(String substring, String[] sources) {
        return in(substring, sources);
    }

    /**
     * 替换最后一次出现的字串
     * Replaces the very last occurrence of validator substring with supplied string.
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceLast(String s, String sub, String with) {
        int i = s.lastIndexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param list   需要处理的列表
     * @param symbol 链接的符号
     * @return 处理后的字符串
     */
    public static String join(Collection list, String symbol) {
        String result = "";
        if (list != null) {
            for (Object o : list) {
                if (o == null) continue;
                String temp = o.toString();
                if (temp.trim().length() > 0) result += (temp + symbol);
            }
            if (result.length() > 1) {
                result = result.substring(0, result.length() - 1);
            }
        }
        return result;
    }

    /**
     * 字符串省略截取
     * 字符串截取到指定长度size+...的形式
     *
     * @param subject 需要处理的字符串
     * @param size    截取的长度
     * @return 处理后的字符串
     */
    public static String subStringOmit(String subject, int size) {
        if (subject != null && subject.length() > size) {
            subject = subject.substring(0, size) + "...";
        }
        return subject;
    }

    /**
     * 截取字符串　超出的字符用symbol代替
     *
     * @param str    需要处理的字符串
     * @param len    字符串长度
     * @param symbol 最后拼接的字符串
     * @return 测试后的字符串
     */
    public static String subStringSymbol(String str, int len, String symbol) {
        String temp = "";
        if (str != null && str.length() > len) {
            temp = str.substring(0, len) + symbol;
        }
        return temp;
    }

    /**
     * 替换一个出现的子串
     *
     * @param s    source string
     * @param sub  substring to replace
     * @param with substring to replace with
     */
    public static String replaceFirst(String s, String sub, String with) {
        int i = s.indexOf(sub);
        if (i == -1) {
            return s;
        }
        return s.substring(0, i) + with + s.substring(i + sub.length());
    }

    /**
     * 字节数组转化为字符串
     */
    public String bytesToString(byte[] bytes) {
        // 错误写法，依赖于平台的默认字符集，换个平台可能就是乱码 : return new String(bytes);
        try {
            return new String(bytes, DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.transform(e);
        }
    }

    /**
     * 字符串转化成字节数组
     */
    public byte[] stringToBytes(String content) {
        // 错误写法，依赖于平台的默认字符集，换个平台得到的字节数组可能就不正确了: return content.getBytes();
        // 正确写法
        try {
            return content.getBytes(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtil.transform(e);
        }
    }

    /**
     * 加双引号
     *
     * @param str
     * @return
     */
    public static String quote(String str) {
        return "\"".concat(str).concat("\"");
    }

    /**
     * append
     *
     * @param builder
     * @param data
     * @return
     */
    public static StringBuilder append(StringBuilder builder, Object... data) {
        if (data == null) {
            return builder;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                builder.append(data[i].toString());
            }
        }
        return builder;
    }

    /**
     * append
     *
     * @param builder
     * @param data
     * @return
     */
    public static StringBuilder append(Object splitter, StringBuilder builder, Object... data) {
        if (data == null) {
            return builder;
        }
        if (builder == null) {
            builder = new StringBuilder();
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
                builder.append(data[i].toString());
            }
            if (i != data.length - 1) {
                builder.append(splitter);
            }
        }
        return builder;
    }

    /**
     * append
     *
     * @param data
     * @return
     */
    public static StringBuilder append(char splitter, Object... data) {
        return append(splitter, null, data);
    }

    /**
     * join With String
     */
    public static String joinWithString(String splitter, Object... data) {
        return append(splitter, null, data).toString();
    }

    /**
     * append
     *
     * @param builder
     * @param data
     * @return
     */
    public static StringBuilder appendWithComma(StringBuilder builder, Object... data) {
        return append(',', builder, data);
    }

    /**
     * 拼接成对的字符串。如joinPart("name","zhangsan","age",1) 则输出 name:zhangsan, age:1
     *
     * @param objects
     * @return
     */
    public static String joinPairs(Object... objects) {
        if (objects == null || objects.length % 2 != 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int count = objects.length / 2;
        for (int i = 0; i < count; i++) {
            Object key = objects[i * 2];
            Object value = objects[i * 2 + 1];
            builder.append(key).append(":").append(value);
            if (i != count - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    /**
     * 下划线转驼峰
     */
    public static String underLineToCamel(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法，效率低于{@link #camelToUnderLine(String)})
     */
    public static String camelToUnderLineSimple(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 驼峰转下划线,效率比上面高
     */
    public static String camelToUnderLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}