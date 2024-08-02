package com.xiaofei.utils;

import org.apache.commons.codec.digest.Md5Crypt;

/**
 * 加密工具类
 *
 * @date 2022/4/2 22:57
 */
public class CodecUtils {
    /**
     * 盐值，格式：$*$*******
     */
    private static final String salt = "$1$731016";

    /**
     * 获取盐值
     */
    public static String getSalt() {
        return CodecUtils.salt;
    }

    /**
     * 生成密码
     */
    public static String encrypt(String pwd) {
        return Md5Crypt.md5Crypt(pwd.getBytes(), salt);
    }
    /**
     * 登录时，查询数据库的salt
     */
    public static String loginEncrypt(String pwd, String salt) {
        return Md5Crypt.md5Crypt(pwd.getBytes(), salt);
    }
}
