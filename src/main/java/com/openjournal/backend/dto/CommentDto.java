package com.openjournal.backend.dto;

import java.time.Instant;

public record CommentDto(
    String content,
    AuthorDto author,
    Instant createdAt
) {}
