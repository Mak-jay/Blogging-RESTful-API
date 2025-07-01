package com.blogapi.payload;

import java.util.Set;

public record PostRequest(
        String title,
        String content,
        Long categoryId,
        Set<Long> tagIds
) {}
