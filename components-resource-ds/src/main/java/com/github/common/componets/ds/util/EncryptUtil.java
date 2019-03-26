package com.github.common.componets.ds.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class EncryptUtil {

    /**
     * 缺省密钥
     */
    private static String DEFAULT_ENCRYPT_KEY = "@sdDxDds8exK9E0tqPLx52WchM";

    /**
     * 加密算法
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 生成密钥
     */
    public static String initkey() throws Exception {
        //实例化密钥生成器
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //初始化密钥生成器:AES要求密钥长度为128,192,256位
        kg.init(128);
        //生成密钥
        SecretKey secretKey = kg.generateKey();
        //获取二进制密钥编码形式
        return Base64.encodeBase64String(secretKey.getEncoded());
    }


    /**
     * 转换密钥
     */
    public static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }


    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) throws Exception {
        //还原密钥
        Key k = toKey(Base64.decodeBase64(key));
        //实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        //初始化Cipher对象，设置为加密模式
        //执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        //还原密钥
        Key k = toKey(Base64.decodeBase64(DEFAULT_ENCRYPT_KEY));
        //实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        //执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
    }


    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) throws Exception {
        Key k = toKey(Base64.decodeBase64(key));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        //执行解密操作
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }


    /**
     * 解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        Key k = toKey(Base64.decodeBase64(DEFAULT_ENCRYPT_KEY));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        ;//执行解密操作
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }
}
