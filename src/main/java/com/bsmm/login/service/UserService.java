package com.bsmm.login.service;

import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<UserDTO> getAll();

    Mono<UserDTO> create(Mono<UserSignup> dto);
}
