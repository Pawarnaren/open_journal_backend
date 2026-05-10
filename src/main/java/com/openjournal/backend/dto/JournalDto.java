package com.openjournal.backend.dto;

import java.time.Instant;
import java.util.List;

public record JournalDto(
    String id,
    String title,
    String content,
    String mood,
    List<ImageDto> images,
    Instant createdAt,
    Instant updatedAt
) {}
