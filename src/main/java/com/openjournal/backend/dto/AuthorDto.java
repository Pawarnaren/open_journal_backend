package com.openjournal.backend.dto;

public record AuthorDto(
    String id,
    String name,
    String username,
    String avatarUrl
) {}
