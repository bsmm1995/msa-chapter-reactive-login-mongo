package com.bsmm.login.domain;

import com.bsmm.login.domain.enums.ERole;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "users")
public class UserEntity {
    @Id
    private String id;

    private String fullName;

    @Email
    private String username;

    private String password;

    private Boolean isActive;

    @Builder.Default()
    private List<ERole> roles = new ArrayList<>();
}
