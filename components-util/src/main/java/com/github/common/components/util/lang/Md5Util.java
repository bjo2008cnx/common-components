package com.github.common.components.util.lang;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类.
 */
@Slf4j
public class Md5Util {

    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final String MD5 = "MD5";

    /**
     * 功能：加盐版的MD5.返回格式为MD5(密码+{盐值})
     *
     * @param password 密码
     * @param salt     盐值
     * @return String
     */
    public static String getMD5StringWithSalt(String password, String salt) {
        AssertUtil.assertNotEmpty(password, "password", salt, "salt");

        if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
            throw new IllegalArgumentException("salt中不能包含 { 或者 }");
        }
        return getMD5String(password + "{" + salt.toString() + "}");
    }

    /**
     * 功能：得到文件的md5值。
     *
     * @param file 文件。
     * @return String
     * @throws IOException 读取文件IO异常时。
     */
    public static String getFileMD5String(File file) {
        try {
            FileInputStream in = new FileInputStream(file);
            FileChannel ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest messagedigest = MessageDigest.getInstance(MD5);
            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest());
        } catch (IOException e) {
            log.error("fail to get file md5", e);
            return "";
        } catch (NoSuchAlgorithmException e) {
            log.error("fail to get file md5", e);
            return "";
        }
    }

    /**
     * 功能：得到一个字符串的MD5值。
     *
     * @param str 字符串
     * @return String
     */
    public static String getMD5String(String str) {
        return getMD5String(str.getBytes());
    }

    private static String getMD5String(byte[] bytes) {
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance(MD5);
            messagedigest.update(bytes);
            return bufferToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}