package com.hjnu.config;

import lombok.extern.slf4j.*;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.*;


@Slf4j
@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * 序列化，使到redis里面读数据的时候可以读到服务器，以字符串的形式读出来而不是十六进制
     *
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<Object,Object>();
        //不应该把redis中的value也设置为StringRedisSerializer()，这样会造成存值的时候只能存字符串
//        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
//        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
