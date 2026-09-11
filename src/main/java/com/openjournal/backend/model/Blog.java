package com.openjournal.backend.model;

import com.openjournal.backend.dto.ImageDto;
import com.openjournal.backend.util.BlogDocumentMapper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "blogs")
public class Blog {

    @Id
    private String id;
    private String title;
    private String content;
    private String excerpt;

    @Indexed
    private Object userId;

    // Object so mixed/legacy Mongo shapes (string vs array vs document) do not fail mapping
    private Object tags;
    private Object images;

    private String coverImagePublicId;
    private String coverImageUrl;

    private Object likeCount;
    private Object comments;

    @CreatedDate
    private Object createdAt;
    @LastModifiedDate
    private Object updatedAt;

    public Blog() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }
    public String getUserId() { return BlogDocumentMapper.toIdString(userId); }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getTags() {
        List<String> normalized = BlogDocumentMapper.toTagList(tags);
        this.tags = normalized;
        return normalized;
    }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<ImageDto> getImages() {
        List<ImageDto> normalized = BlogDocumentMapper.toImageList(images);
        this.images = normalized;
        return normalized;
    }
    public void setImages(List<ImageDto> images) { this.images = images; }
    public String getCoverImagePublicId() { return coverImagePublicId; }
    public void setCoverImagePublicId(String coverImagePublicId) { this.coverImagePublicId = coverImagePublicId; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }
    public int getLikeCount() { return BlogDocumentMapper.toLikeCount(likeCount); }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public List<Comment> getComments() {
        List<Comment> normalized = BlogDocumentMapper.toCommentList(comments);
        this.comments = normalized;
        return normalized;
    }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public Instant getCreatedAt() { return BlogDocumentMapper.toInstant(createdAt); }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return BlogDocumentMapper.toInstant(updatedAt); }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
