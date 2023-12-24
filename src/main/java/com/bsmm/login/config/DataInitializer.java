package com.bsmm.login.config;

import com.bsmm.login.domain.User;
import com.bsmm.login.repository.UserRepository;
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

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        log.info("start data initialization...");


        var userFlux = this.users.deleteAll()
                .thenMany(
                        Flux.just("admin")
                                .flatMap(username -> {
                                    List<String> roles = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
                                    User user = User.builder()
                                            .roles(roles)
                                            .fullName(username)
                                            .password(passwordEncoder.encode("Admin_1234"))
                                            .username(username + "@email.com")
                                            .build();

                                    return this.users.save(user);
                                })
                );

        userFlux.doOnSubscribe(data -> log.info("data:" + data))
                .subscribe(
                        data -> log.info("data:" + data), err -> log.error("error:" + err),
                        () -> log.info("done initialization...")
                );

    }

}
