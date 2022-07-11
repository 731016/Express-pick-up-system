package com.xiaofei.cache;

import com.xiaofei.utils.RedisUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * TODO 类描述
 *
 * @date 2022/4/15 21:08
 */
public class SchoolServiceCache {
    @Resource
    private RedisUtils redisUtils;
}
