package com.blogapi.payload;

import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    String body,
    LocalDateTime createdAt,
    String authorUsername,
    Long postId
) {} 