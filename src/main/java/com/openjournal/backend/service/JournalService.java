package com.openjournal.backend.service;

import com.openjournal.backend.dto.CreateJournalRequest;
import com.openjournal.backend.dto.ImageDto;
import com.openjournal.backend.dto.JournalDto;
import com.openjournal.backend.exception.ForbiddenException;
import com.openjournal.backend.exception.ResourceNotFoundException;
import com.openjournal.backend.model.Journal;
import com.openjournal.backend.repository.JournalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JournalService {

    private static final Logger log = LoggerFactory.getLogger(JournalService.class);

    private final JournalRepository journalRepository;

    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public Page<JournalDto> getJournalsByUser(String userId, Pageable pageable) {
        return journalRepository.findByUserId(userId, pageable)
                .map(this::mapToDto);
    }

    public JournalDto getJournalById(String id, String userId) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found"));
        if (!journal.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to access this journal");
        }
        return mapToDto(journal);
    }

    public JournalDto createJournal(CreateJournalRequest request, String userId) {
        log.info("Creating journal - title: {}, mood: {}, images count: {}", 
                request.title(), request.mood(), 
                request.images() != null ? request.images().size() : "NULL");
        
        if (request.images() != null) {
            for (ImageDto img : request.images()) {
                log.info("Image received - public_id: {}, secure_url: {}", img.public_id(), img.secure_url());
            }
        }

        Journal journal = new Journal();
        journal.setTitle(request.title());
        journal.setContent(request.content());
        journal.setMood(request.mood());
        journal.setImages(request.images() != null ? request.images() : Collections.emptyList());
        journal.setUserId(userId);
        
        Journal saved = journalRepository.save(journal);
        log.info("Journal saved - id: {}, images in DB: {}", saved.getId(), saved.getImages().size());
        
        return mapToDto(saved);
    }

    public JournalDto updateJournal(String id, CreateJournalRequest request, String userId) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found"));
        
        if (!journal.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to edit this journal");
        }

        journal.setTitle(request.title());
        journal.setContent(request.content());
        journal.setMood(request.mood());
        journal.setImages(request.images() != null ? request.images() : Collections.emptyList());

        return mapToDto(journalRepository.save(journal));
    }

    public void deleteJournal(String id, String userId) {
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal not found"));
        
        if (!journal.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to delete this journal");
        }

        journalRepository.delete(journal);
    }

    private JournalDto mapToDto(Journal journal) {
        return new JournalDto(
                journal.getId(),
                journal.getTitle(),
                journal.getContent(),
                journal.getMood(),
                journal.getImages(),
                journal.getCreatedAt(),
                journal.getUpdatedAt()
        );
    }
}
