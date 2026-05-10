package com.openjournal.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateBlogRequest(
    @NotBlank String title,
    @NotBlank String content,
    String excerpt,
    List<String> tags,
    String coverImagePublicId,
    String coverImageUrl,
    List<ImageDto> images
) {}
