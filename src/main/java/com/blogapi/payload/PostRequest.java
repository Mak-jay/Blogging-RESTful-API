package com.blogapi.payload;

import java.util.Set;

public record PostRequest(
        String title,
        String content,
        Boolean published,
        Long categoryId,
        Set<Long> tagIds
) {}
