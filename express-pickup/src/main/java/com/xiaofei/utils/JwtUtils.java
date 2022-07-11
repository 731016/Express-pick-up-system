package com.xiaofei.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * JWT工具类
 * 用于生成和校验token
 *
 * @date 2022/3/31 20:45
 */
public class JwtUtils {
    // Token过期时间1分钟
    public static final long EXPIRE_TIME = 10 * 60 * 1000;

    /**
     * 校验token是否正确
     * @param token
     * @param username
     * @param secret
     * @return
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 设置加密算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userName", username)
                    .build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 生成签名,30min后过期
     * @param username
     * @param secret
     * @return
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("userName", username)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    /**
     * 获得用户名
     * @param token
     * @return
     */
    public static String getUserNameByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userName")
                    .asString();
        }
        return null;
    }
}
