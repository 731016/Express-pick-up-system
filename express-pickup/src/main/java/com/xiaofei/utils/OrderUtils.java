package com.xiaofei.utils;

import java.util.Date;
import java.util.Random;

/**
 * TODO 类描述
 *
 * @date 2022/4/9 21:53
 */
public class OrderUtils {
    public static String genSerialNumber() {
        String str = System.currentTimeMillis() + "";
        String num = new Random().nextInt(731016) + "";
        return str + num;
    }
}
