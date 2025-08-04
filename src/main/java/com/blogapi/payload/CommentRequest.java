package com.blogapi.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
    @NotBlank(message = "Comment body cannot be empty")
    @Size(min = 1, max = 1000, message = "Comment body must be between 1 and 1000 characters")
    String body
) {} 