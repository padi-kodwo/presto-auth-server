package com.presto.auth.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {


    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, T> redisTemplate = new RedisTemplate<String, T>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(1200000))
                .serializeValuesWith(jsonSerializer)
                .disableCachingNullValues();
        cacheConfig.prefixKeysWith("INVM_");
        return cacheConfig;
    }
    @Bean
    public RedisCacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = RedisCacheManager.builder(redisTemplate.getConnectionFactory())
                .cacheDefaults(cacheConfiguration())
                .transactionAware()
                .build();

        return rcm;
    }

}
