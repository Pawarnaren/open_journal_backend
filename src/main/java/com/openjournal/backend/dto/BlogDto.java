package com.openjournal.backend.dto;

import java.time.Instant;
import java.util.List;

public record BlogDto(
    String id,
    String title,
    String content,
    String excerpt,
    List<String> tags,
    AuthorDto author,
    int likeCount,
    String coverImagePublicId,
    String coverImageUrl,
    List<CommentDto> comments,
    List<ImageDto> images,
    Instant createdAt,
    Instant updatedAt
) {}
