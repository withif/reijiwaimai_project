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
    public RedisTemplate<Object,Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Object> redisTemplate=new RedisTemplate();
        //默认的key序列化器是JdkSerializationRedisSerializers
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
