package com.openjournal.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
    @NotBlank String content
) {}
