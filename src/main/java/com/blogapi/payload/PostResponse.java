package com.blogapi.payload;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponse(
        Long id,
        String title,
        String slug,
        String content,
        Boolean published,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String authorName,
        String categoryName,
        Set<String> tagNames,
        Integer commentCount
) {}
