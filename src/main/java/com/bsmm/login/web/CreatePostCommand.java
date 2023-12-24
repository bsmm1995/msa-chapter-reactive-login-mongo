package com.bsmm.login.web;

import jakarta.validation.constraints.NotBlank;

public record CreatePostCommand(
        @NotBlank String title,
        @NotBlank String content
) {
}
