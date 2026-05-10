package com.openjournal.backend.controller;

import com.openjournal.backend.dto.BlogDto;
import com.openjournal.backend.dto.CommentRequest;
import com.openjournal.backend.dto.CreateBlogRequest;
import com.openjournal.backend.security.CustomUserDetails;
import com.openjournal.backend.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public Page<BlogDto> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tag) {
        return blogService.getAllBlogs(search, tag, PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @GetMapping("/me")
    public Page<BlogDto> getMyBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return blogService.getBlogsByUser(userDetails.getId(), PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @GetMapping("/{id}")
    public BlogDto getBlogById(@PathVariable String id) {
        return blogService.getBlogById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogDto createBlog(
            @Valid @RequestBody CreateBlogRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return blogService.createBlog(request, userDetails.getId());
    }

    @PutMapping("/{id}")
    public BlogDto updateBlog(
            @PathVariable String id,
            @Valid @RequestBody CreateBlogRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return blogService.updateBlog(id, request, userDetails.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlog(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        blogService.deleteBlog(id, userDetails.getId());
    }

    @PostMapping("/{id}/like")
    public BlogDto likeBlog(@PathVariable String id) {
        return blogService.likeBlog(id);
    }

    @PostMapping("/{id}/comments")
    public BlogDto addComment(
            @PathVariable String id,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return blogService.addComment(id, request, userDetails.getId());
    }

    @GetMapping("/tags")
    public List<String> getTags() {
        return blogService.getTags();
    }
}
