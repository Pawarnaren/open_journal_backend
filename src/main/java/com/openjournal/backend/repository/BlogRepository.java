package com.openjournal.backend.repository;

import com.openjournal.backend.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog, String> {
    Page<Blog> findByUserId(String userId, Pageable pageable);
    Page<Blog> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Blog> findByTagsContainingIgnoreCase(String tag, Pageable pageable);
    Page<Blog> findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(String title, String tag, Pageable pageable);
}
