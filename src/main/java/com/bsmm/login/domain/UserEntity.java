package com.bsmm.login.domain;

import com.bsmm.login.domain.enums.ERole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "users")
public class UserEntity {
    @Id
    private String id;

    private String fullName;
    @Email
    private String username;

    @JsonIgnore
    private String password;

    @Builder.Default()
    private boolean isActive = Boolean.TRUE;

    @Builder.Default()
    private List<ERole> roles = new ArrayList<>();
}
