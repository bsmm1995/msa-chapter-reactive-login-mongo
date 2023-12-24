package com.bsmm.login.service.dto;

import com.bsmm.login.domain.enums.ERole;
import com.bsmm.login.util.Constants;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserSignup implements Serializable {
    @NotBlank
    @Size(max = 150)
    private String fullName;

    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    @NotEmpty
    private Set<ERole> roles;

    @NotBlank
    @Size(min = 8, max = 25)
    @Pattern(regexp = Constants.COMPLEX_PASSWORD_REGEX)
    private String password;
}
