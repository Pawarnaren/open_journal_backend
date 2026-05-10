package com.openjournal.backend.dto.response;

public record AuthResponse(
    String token,
    UserDto user
) {}
