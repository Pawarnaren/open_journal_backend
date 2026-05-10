package com.openjournal.backend.model;

import org.springframework.data.annotation.CreatedDate;
import java.time.Instant;

public class Comment {
    private String content;
    private String userId;

    @CreatedDate
    private Instant createdAt;

    public Comment() {}

    public Comment(String content, String userId) {
        this.content = content;
        this.userId = userId;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
