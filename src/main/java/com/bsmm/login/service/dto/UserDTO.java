package com.bsmm.login.service.dto;

import com.bsmm.login.domain.enums.ERole;

import java.util.Set;

public record UserDTO(
        String id,
        String fullName,
        String username,
        Boolean isActive,
        Set<ERole> roles) {
}
