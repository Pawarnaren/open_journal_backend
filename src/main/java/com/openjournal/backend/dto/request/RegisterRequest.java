package com.openjournal.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Size(min = 3, max = 20) String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6, max = 40) String password,
    String avatarUrl
) {}
