package com.github.common.componets.config.encrypt;

import com.ctrip.framework.apollo.core.utils.StringUtils;
import org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 配置文件加密工具
 */
public class ConfigEncryptUtil {

    /**
     * 获取加密参数
     *
     * @param input
     * @return
     */
    public static Map getEncryptedParams(String input) {
        //输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        PrintStream cacheStream = new PrintStream(byteArrayOutputStream);
        //更换数据输出位置
        System.setOut(cacheStream);

        //加密参数组装
        String[] args = {"input=" + input, "password=" + EncryptConstant.DEFAULT_PASSWORD, "algorithm=" + EncryptConstant.ALGORITHM};
        JasyptPBEStringEncryptionCLI.main(args);

        //执行加密后的输出
        String message = byteArrayOutputStream.toString();
        String str = replaceBlank(message);
        int index = str.lastIndexOf("-");

        //返回加密后的数据
        Map result = new HashMap();
        result.put("output", str.substring(index + 1));
        result.put("password", EncryptConstant.DEFAULT_PASSWORD);
        return result;
    }

    /**
     * 替换制表符、空格、换行符
     *
     * @param str
     * @return
     */
    private static String replaceBlank(String str) {
        String dest = "";
        if (!StringUtils.isEmpty(str)) {
            Matcher matcher = EncryptConstant.BLANK_PATTERN.matcher(str);
            dest = matcher.replaceAll("");
        }
        return dest;
    }

    /**
     * 示例
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getEncryptedParams("kl"));//print : {output=Ore69lUopDHL5R8Bw/G3bQ==, password=klklklklklklklkl}
    }
}
