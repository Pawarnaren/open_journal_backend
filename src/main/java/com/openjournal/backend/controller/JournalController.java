package com.openjournal.backend.controller;

import com.openjournal.backend.dto.CreateJournalRequest;
import com.openjournal.backend.dto.JournalDto;
import com.openjournal.backend.security.CustomUserDetails;
import com.openjournal.backend.service.JournalService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    private final JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @GetMapping
    public Page<JournalDto> getJournals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return journalService.getJournalsByUser(
                userDetails.getId(),
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }

    @GetMapping("/{id}")
    public JournalDto getJournalById(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return journalService.getJournalById(id, userDetails.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JournalDto createJournal(
            @Valid @RequestBody CreateJournalRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return journalService.createJournal(request, userDetails.getId());
    }

    @PutMapping("/{id}")
    public JournalDto updateJournal(
            @PathVariable String id,
            @Valid @RequestBody CreateJournalRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return journalService.updateJournal(id, request, userDetails.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJournal(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        journalService.deleteJournal(id, userDetails.getId());
    }
}
