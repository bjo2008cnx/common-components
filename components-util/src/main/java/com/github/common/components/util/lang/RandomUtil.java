package com.github.common.components.util.lang;

import java.util.Random;
import java.util.UUID;

/**
 * 随机工具类
 */
public class RandomUtil {

    public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERCHAR = "0123456789";
    private static final String NUMBERS = "0123456789";
    private static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String HYBRID_CHARS = LOWER_CHARS + UPPER_CHARS;
    private static final String HYBRID_CHARS_NUMBERS = NUMBERS + HYBRID_CHARS;

    /**
     * 生成一个随机的字符串
     *
     * @param length
     * @param isSuperCase
     * @return
     */
    public static String randomString(int length, boolean isSuperCase) {
        return doRandom(length, isSuperCase ? UPPER_CHARS : LOWER_CHARS);
    }

    /**
     * 随机产生几位数字：例：maxLength=3,则结果可能是 12
     */
    public static final int randomNumber(int maxLength) {
        Random random = new Random();
        return random.nextInt(maxLength);
    }

    /**
     * 随机产生区间数字：例：minNumber=1,maxNumber=2,则结果可能是 1、2,包括首尾。
     */
    public static int randomRegionNumber(int minNumber, int maxNumber) {
        return minNumber + randomNumber(maxNumber);
    }

    /**
     * 随机产生几位字符串：例：maxLength=3,则结果可能是 aAz
     *
     * @param maxLength 传入数必须是正数。
     */
    public static String randomString(int maxLength) {
        String source = HYBRID_CHARS;
        return doRandom(maxLength, source);
    }

    /**
     * 随机产生几位字符串：例：maxLength=3,则结果可能是 aAz
     *
     * @param length 传入数必须是正数。
     */
    public static String randomStringLength(int length) {
        String source = HYBRID_CHARS;
        return doRandom(length, source);
    }

    /**
     * 随机产生随机数字+字母：例：maxLength=3,则结果可能是 1Az
     *
     * @param maxLength 传入数必须是正数。
     */
    public static String randomStringAndNumber(int maxLength) {
        return doRandom(maxLength, HYBRID_CHARS_NUMBERS);
    }

    /**
     * 自定义随机产生结果
     */
    public static String randomResultByCustom(String customString, int maxLength) {
        return doRandom(maxLength, customString);
    }

    /**
     * 随机生成字符串
     */
    private static String doRandom(int maxLength, String source) {
        StringBuilder sb = new StringBuilder(100);
        int length = source.length();
        for (int i = 0; i < maxLength; i++) {
            final int number = randomNumber(length);
            sb.append(source.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成制定范围内的随机数
     *
     * @param scopeMin
     * @param scoeMax
     * @return
     */
    public static int integer(int scopeMin, int scoeMax) {
        Random random = new Random();
        return (random.nextInt(scoeMax) % (scoeMax - scopeMin + 1) + scopeMin);
    }

    /**
     * 返回固定长度的数字
     *
     * @param length
     * @return
     */
    public static String number(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String randomStr(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String mixString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(LETTERCHAR.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String lowerString(int length) {
        return mixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String upperString(int length) {
        return mixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length 字符串长度
     * @return 纯0字符串
     */
    public static String zeroString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuilder sb = new StringBuilder();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(zeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(int num, int fixdlenth) {
        StringBuilder sb = new StringBuilder();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(zeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 每次生成的len位数都不相同
     *
     * @param param
     * @return 定长的数字
     */
    public static int getNotSimple(int[] param, int len) {
        Random rand = new Random();
        for (int i = param.length; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = param[index];
            param[index] = param[i - 1];
            param[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < len; i++) {
            result = result * 10 + param[i];
        }
        return result;
    }

    /**
     * 返回一个UUID
     *
     * @return 小写的UUID
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
}
