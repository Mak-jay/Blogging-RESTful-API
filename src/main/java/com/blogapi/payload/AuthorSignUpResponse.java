package com.blogapi.payload;

import com.blogapi.model.ROLE_TYPE;

public record AuthorSignUpResponse(Long id, String username, String email, ROLE_TYPE roleType, Boolean isEnable) {
}
