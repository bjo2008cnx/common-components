package com.github.common.componets.lock.redis;

/**
 * Reids Lock 常量
 *
 */
public class RedisLockConstant {
    /**
     * 创建Key Lua Script
     */
    public static final String CREATE_KEY_LUA = ""
            + "\nlocal r = tonumber(redis.call('SETNX', KEYS[1],ARGV[1]));"
            + "\nredis.call('PEXPIRE',KEYS[1],ARGV[2]);"
            + "\nreturn r";

    /**
     * Unlock Lua Script
     */
    public static final String UNLOCK_LUA = ""
            + "\nlocal v = redis.call('GET', KEYS[1]);"
            + "\nlocal r= 0;"
            + "\nif v == ARGV[1] then"
            + "\nr =redis.call('DEL',KEYS[1]);"
            + "\nend"
            + "\nreturn r";

    /**
     * characters
     */
    public final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z'};
}
