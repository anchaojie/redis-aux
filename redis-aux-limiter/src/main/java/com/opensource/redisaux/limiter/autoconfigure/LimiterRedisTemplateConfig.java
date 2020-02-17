package com.opensource.redisaux.limiter.autoconfigure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensource.redisaux.common.BloomFilterConstants;
import com.opensource.redisaux.common.LimiterConstants;
import io.lettuce.core.RedisConnectionException;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.security.auth.callback.Callback;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Supplier;

/**
 * @author lulu
 * @Date 2020/2/16 16:47
 */
@Configuration
public class LimiterRedisTemplateConfig {
    @Bean(name = LimiterConstants.LIMITER)
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper().setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper().setDefaultTyping(ObjectMapper.DefaultTypeResolverBuilder.noTypeInfoBuilder());
        objectMapper().setDateFormat(dateFormat);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setStringSerializer(stringRedisSerializer);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
