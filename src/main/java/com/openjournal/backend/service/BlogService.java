package com.openjournal.backend.service;

import com.openjournal.backend.dto.*;
import com.openjournal.backend.exception.ForbiddenException;
import com.openjournal.backend.exception.ResourceNotFoundException;
import com.openjournal.backend.model.Blog;
import com.openjournal.backend.model.Comment;
import com.openjournal.backend.model.User;
import com.openjournal.backend.repository.BlogRepository;
import com.openjournal.backend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    public Page<BlogDto> getAllBlogs(String search, String tag, Pageable pageable) {
        Page<Blog> blogs;
        if (search != null && tag != null) {
            blogs = blogRepository.findByTitleContainingIgnoreCaseAndTagsContainingIgnoreCase(search, tag, pageable);
        } else if (search != null) {
            blogs = blogRepository.findByTitleContainingIgnoreCase(search, pageable);
        } else if (tag != null) {
            blogs = blogRepository.findByTagsContainingIgnoreCase(tag, pageable);
        } else {
            blogs = blogRepository.findAll(pageable);
        }
        return blogs.map(this::mapToDto);
    }

    public Page<BlogDto> getBlogsByUser(String userId, Pageable pageable) {
        return blogRepository.findByUserId(userId, pageable).map(this::mapToDto);
    }

    public BlogDto getBlogById(String id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        return mapToDto(blog);
    }

    public BlogDto createBlog(CreateBlogRequest request, String userId) {
        Blog blog = new Blog();
        blog.setTitle(request.title());
        blog.setContent(request.content());
        blog.setExcerpt(request.excerpt());
        blog.setTags(request.tags() != null ? request.tags() : Collections.emptyList());
        blog.setCoverImagePublicId(request.coverImagePublicId());
        blog.setCoverImageUrl(request.coverImageUrl());
        blog.setImages(request.images() != null ? request.images() : Collections.emptyList());
        blog.setUserId(userId);
        return mapToDto(blogRepository.save(blog));
    }

    public BlogDto updateBlog(String id, CreateBlogRequest request, String userId) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        if (!blog.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to edit this blog");
        }

        blog.setTitle(request.title());
        blog.setContent(request.content());
        blog.setExcerpt(request.excerpt());
        blog.setTags(request.tags() != null ? request.tags() : Collections.emptyList());
        blog.setCoverImagePublicId(request.coverImagePublicId());
        blog.setCoverImageUrl(request.coverImageUrl());
        blog.setImages(request.images() != null ? request.images() : Collections.emptyList());

        return mapToDto(blogRepository.save(blog));
    }

    public void deleteBlog(String id, String userId) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        if (!blog.getUserId().equals(userId)) {
            throw new ForbiddenException("You don't have permission to delete this blog");
        }

        blogRepository.delete(blog);
    }

    public BlogDto likeBlog(String id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        blog.setLikeCount(blog.getLikeCount() + 1);
        return mapToDto(blogRepository.save(blog));
    }

    public BlogDto addComment(String id, CommentRequest request, String userId) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        
        Comment comment = new Comment(request.content(), userId);
        blog.getComments().add(comment);
        return mapToDto(blogRepository.save(blog));
    }

    public List<String> getTags() {
        return blogRepository.findAll().stream()
                .flatMap(blog -> blog.getTags().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private BlogDto mapToDto(Blog blog) {
        User author = userRepository.findById(blog.getUserId()).orElse(null);
        AuthorDto authorDto = null;
        if (author != null) {
            authorDto = new AuthorDto(
                    author.getId(),
                    author.getUsername(),
                    author.getUsername(),
                    author.getAvatarUrl()
            );
        }

        List<CommentDto> commentDtos = blog.getComments().stream().map(c -> {
            User commentAuthor = userRepository.findById(c.getUserId()).orElse(null);
            AuthorDto commentAuthorDto = null;
            if (commentAuthor != null) {
                commentAuthorDto = new AuthorDto(
                        commentAuthor.getId(),
                        commentAuthor.getUsername(),
                        commentAuthor.getUsername(),
                        commentAuthor.getAvatarUrl()
                );
            }
            return new CommentDto(
                    c.getContent(),
                    commentAuthorDto,
                    c.getCreatedAt()
            );
        }).collect(Collectors.toList());

        return new BlogDto(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getExcerpt(),
                blog.getTags(),
                authorDto,
                blog.getLikeCount(),
                blog.getCoverImagePublicId(),
                blog.getCoverImageUrl(),
                commentDtos,
                blog.getImages(),
                blog.getCreatedAt(),
                blog.getUpdatedAt()
        );
    }
}
