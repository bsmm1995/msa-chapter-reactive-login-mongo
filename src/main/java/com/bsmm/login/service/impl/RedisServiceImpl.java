package com.bsmm.login.service.impl;

import com.bsmm.login.security.JwtTokenProvider;
import com.bsmm.login.service.RedisService;
import com.bsmm.login.service.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final ReactiveRedisOperations<String, LoginResponse> redisOperations;

    private final JwtTokenProvider tokenProvider;

    @Override
    public Mono<Boolean> saveSession(LoginResponse data) {
        return redisOperations.opsForValue().set(tokenProvider.getClaimId(data.accessToken()), data);
    }

    @Override
    public Mono<Long> deleteSession(String token) {
        return redisOperations.delete(tokenProvider.getClaimId(token));
    }

    @Override
    public Mono<Boolean> existSession(String token) {
        return redisOperations.opsForValue().get(tokenProvider.getClaimId(token))
                .hasElement();
    }
}
