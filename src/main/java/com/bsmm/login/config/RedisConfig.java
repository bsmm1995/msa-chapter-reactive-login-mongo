package com.bsmm.login.config;

import com.bsmm.login.service.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    @Primary
    public ReactiveRedisOperations<String, LoginResponse> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<LoginResponse> serializer = new Jackson2JsonRedisSerializer<>(LoginResponse.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, LoginResponse> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, LoginResponse> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}