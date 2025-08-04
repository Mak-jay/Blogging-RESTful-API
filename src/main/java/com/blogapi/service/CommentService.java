package com.blogapi.service;

import java.util.List;

import com.blogapi.payload.CommentRequest;
import com.blogapi.payload.CommentResponse;

public interface CommentService {
    
    CommentResponse createComment(Long postId, CommentRequest commentRequest);
    
    CommentResponse updateComment(Long commentId, CommentRequest commentRequest);
    
    void deleteComment(Long commentId);
    
    CommentResponse getCommentById(Long commentId);
    
    List<CommentResponse> getCommentsByPostId(Long postId);
    
    List<CommentResponse> getCommentsByUserId(Long userId);
} 