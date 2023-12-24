package com.bsmm.login.service.impl;


import com.bsmm.login.domain.UserEntity;
import com.bsmm.login.exception.UserFoundException;
import com.bsmm.login.repository.UserRepository;
import com.bsmm.login.service.UserService;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import com.bsmm.login.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Flux<UserDTO> getAll() {
        return userRepository.findAll().map(UserMapper.INSTANCE::toDto);
    }

    @Override
    @Transactional
    public Mono<UserDTO> create(Mono<UserSignup> signupMono) {
        return signupMono.filterWhen(userSignup -> userRepository.existsByUsername(userSignup.username())
                        .map(Boolean.FALSE::equals))
                .switchIfEmpty(Mono.error(new UserFoundException("Usuario ya en uso")))
                .flatMap(userSignup -> {
                    UserEntity newUser = UserMapper.INSTANCE.toEntity(userSignup);
                    return userRepository.save(newUser.withPassword(passwordEncoder.encode(userSignup.password())));
                })
                .map(UserMapper.INSTANCE::toDto);
    }
}
