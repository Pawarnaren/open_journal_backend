package com.openjournal.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateJournalRequest(
    @NotBlank String title,
    @NotBlank String content,
    String mood,
    List<ImageDto> images
) {}
