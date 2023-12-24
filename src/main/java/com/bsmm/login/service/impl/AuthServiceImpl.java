package com.bsmm.login.service.impl;


import com.bsmm.login.repository.UserRepository;
import com.bsmm.login.security.JwtTokenProvider;
import com.bsmm.login.service.AuthService;
import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider tokenProvider;
    private final ReactiveAuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public Mono<LoginResponse> loginUser(Mono<LoginRequest> loginRequest) {
        return loginRequest
                .flatMap(login -> authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                login.username(), login.password()))
                        .map(tokenProvider::getResponse));
    }

    @Override
    public Mono<LoginResponse> refreshToken(Mono<String> stringMono) {
        return stringMono.filterWhen(token -> Mono.just(tokenProvider.validateToken(token)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token caducado")))
                .flatMap(token -> userRepository.findByUsername(tokenProvider.getUserNameFromJwt(token)))
                .flatMap(user -> Mono.just(tokenProvider.getResponse(null)));
    }

    @Override
    public Mono<Void> logoutUser(Mono<String> stringMono) {
        log.info("Eliminar session desde REDIS");
        return Mono.empty();
    }

}
