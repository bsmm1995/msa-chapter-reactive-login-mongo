package com.bsmm.login.controller;

import com.bsmm.login.service.AuthService;
import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody Mono<LoginRequest> loginRequestMono) {
        return authService.loginUser(loginRequestMono)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/auth/refresh-token")
    public Mono<LoginResponse> refreshToken(@RequestHeader("Authorization") String token) {
        return authService.refreshToken(Mono.just(token));
    }

    @PostMapping("/auth/sign-out")
    public Mono<ResponseEntity<Void>> logoutUser(@RequestHeader("Authorization") String token) {
        return authService.logoutUser(Mono.just(token)).then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
    }
}
