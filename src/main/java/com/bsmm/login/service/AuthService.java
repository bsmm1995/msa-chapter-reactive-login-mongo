package com.bsmm.login.service;

import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LoginResponse> loginUser(Mono<LoginRequest> loginRequest);

    Mono<LoginResponse> refreshToken(Mono<String> token);

    Mono<Void> logoutUser(Mono<String> token);
}
