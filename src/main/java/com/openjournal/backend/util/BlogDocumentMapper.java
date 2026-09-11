package com.openjournal.backend.util;

import com.openjournal.backend.dto.ImageDto;
import com.openjournal.backend.model.Comment;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class BlogDocumentMapper {

    private BlogDocumentMapper() {}

    public static List<String> toTagList(Object raw) {
        List<String> tags = new ArrayList<>();
        if (raw == null) {
            return tags;
        }
        if (raw instanceof String s) {
            if (!s.isBlank()) {
                for (String part : s.split(",")) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        tags.add(trimmed);
                    }
                }
            }
            return tags;
        }
        if (raw instanceof Collection<?> collection) {
            for (Object item : collection) {
                if (item != null && !item.toString().isBlank()) {
                    tags.add(item.toString());
                }
            }
        }
        return tags;
    }

    public static List<ImageDto> toImageList(Object raw) {
        List<ImageDto> images = new ArrayList<>();
        if (raw == null) {
            return images;
        }
        if (raw instanceof Collection<?> collection) {
            for (Object item : collection) {
                ImageDto image = toImage(item);
                if (image != null) {
                    images.add(image);
                }
            }
            return images;
        }
        ImageDto single = toImage(raw);
        if (single != null) {
            images.add(single);
        }
        return images;
    }

    public static List<Comment> toCommentList(Object raw) {
        List<Comment> comments = new ArrayList<>();
        if (raw == null) {
            return comments;
        }
        if (raw instanceof Collection<?> collection) {
            for (Object item : collection) {
                Comment comment = toComment(item);
                if (comment != null) {
                    comments.add(comment);
                }
            }
        }
        return comments;
    }

    public static Instant toInstant(Object raw) {
        if (raw instanceof Instant instant) {
            return instant;
        }
        if (raw instanceof Date date) {
            return date.toInstant();
        }
        if (raw instanceof String s && !s.isBlank()) {
            try {
                return Instant.parse(s);
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    public static String toIdString(Object raw) {
        return raw == null ? null : raw.toString();
    }

    public static int toLikeCount(Object raw) {
        if (raw instanceof Number number) {
            return number.intValue();
        }
        if (raw instanceof Collection<?> collection) {
            return collection.size();
        }
        return 0;
    }

    private static ImageDto toImage(Object raw) {
        if (raw == null) {
            return null;
        }
        if (raw instanceof ImageDto imageDto) {
            return imageDto;
        }
        if (raw instanceof String url && !url.isBlank()) {
            return new ImageDto(null, url);
        }
        if (raw instanceof Map<?, ?> map) {
            String publicId = stringValue(map, "public_id", "publicId");
            String url = stringValue(map, "secure_url", "secureUrl", "url", "src");
            if (publicId == null && url == null) {
                return null;
            }
            return new ImageDto(publicId, url);
        }
        return null;
    }

    private static Comment toComment(Object raw) {
        if (raw == null) {
            return null;
        }
        if (raw instanceof Comment comment) {
            return comment;
        }
        if (raw instanceof String text) {
            if (text.isBlank()) {
                return null;
            }
            return new Comment(text, null);
        }
        if (raw instanceof Map<?, ?> map) {
            String content = stringValue(map, "content", "text", "body", "comment");
            String userId = stringValue(map, "userId", "user_id", "authorId");
            if (content == null) {
                return null;
            }
            Comment comment = new Comment(content, userId);
            Object created = first(map, "createdAt", "created_at", "date");
            if (created instanceof Instant instant) {
                comment.setCreatedAt(instant);
            } else if (created instanceof Date date) {
                comment.setCreatedAt(date.toInstant());
            }
            return comment;
        }
        return null;
    }

    private static String stringValue(Map<?, ?> map, String... keys) {
        Object value = first(map, keys);
        return value == null ? null : value.toString();
    }

    private static Object first(Map<?, ?> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key) && map.get(key) != null) {
                return map.get(key);
            }
        }
        if (map instanceof Document document) {
            for (String key : keys) {
                if (document.containsKey(key) && document.get(key) != null) {
                    return document.get(key);
                }
            }
        }
        return null;
    }
}
