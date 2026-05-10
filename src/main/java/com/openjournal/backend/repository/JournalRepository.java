package com.openjournal.backend.repository;

import com.openjournal.backend.model.Journal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepository extends MongoRepository<Journal, String> {
    Page<Journal> findByUserId(String userId, Pageable pageable);
}
