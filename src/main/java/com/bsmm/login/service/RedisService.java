package com.bsmm.login.service;

import com.bsmm.login.service.dto.LoginResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface RedisService {
    Mono<Boolean> saveSession(LoginResponse data);

    Mono<Long> deleteSession(String key);

    Mono<Boolean> existSession(String token);
}
