package com.openjournal.backend.dto.response;

import java.time.Instant;

public record UserDto(
    String id,
    String username,
    String email,
    String role,
    String avatarUrl,
    Instant createdAt
) {}
