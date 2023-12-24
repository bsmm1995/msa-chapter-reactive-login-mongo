package com.bsmm.login.controller;

import com.bsmm.login.service.UserService;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Flux<UserDTO> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public Mono<ResponseEntity<UserDTO>> create(@RequestBody @Valid UserSignup dto) {
        return userService.create(Mono.just(dto))
                .map(userDTO -> ResponseEntity.status(HttpStatus.CREATED).body(userDTO));
    }


}
