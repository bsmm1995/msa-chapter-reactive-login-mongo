package com.bsmm.login.service.dto;

import com.bsmm.login.domain.enums.ERole;
import com.bsmm.login.util.Constants;
import jakarta.validation.constraints.*;

import java.util.Set;


public record UserSignup(
    @NotBlank
    @Size(max = 150)
    String fullName,

    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    String username,

    @NotNull
    @NotEmpty
    Set<ERole> roles,

    @NotBlank
    @Size(min = 8, max = 25)
    @Pattern(regexp = Constants.COMPLEX_PASSWORD_REGEX)
    String password) {
}
