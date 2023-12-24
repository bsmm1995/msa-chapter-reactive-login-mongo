package com.bsmm.login.config;

import com.bsmm.login.domain.UserEntity;
import com.bsmm.login.domain.enums.ERole;
import com.bsmm.login.repository.UserRepository;
import com.bsmm.login.security.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository users;

    private final PasswordEncoder passwordEncoder;

    private final JwtProperties jwtProperties;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        var userFlux = this.users.deleteAll()
                .thenMany(
                        Flux.just(jwtProperties.getUser())
                                .flatMap(username -> {
                                    List<ERole> roles = Arrays.asList(ERole.ROLE_USER, ERole.ROLE_ADMIN);
                                    UserEntity user = UserEntity.builder()
                                            .roles(roles)
                                            .fullName(username)
                                            .password(passwordEncoder.encode(jwtProperties.getPassword()))
                                            .username(username + "@email.com")
                                            .build();

                                    return this.users.save(user);
                                })
                );

        userFlux.doOnSubscribe(data -> log.info("data:" + data))
                .subscribe(
                        data -> log.info("Success: " + data),
                        err -> log.error("Error: " + err),
                        () -> log.info("OK...")
                );
    }
}
