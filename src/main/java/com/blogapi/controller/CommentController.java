package com.blogapi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogapi.payload.CommentRequest;
import com.blogapi.payload.CommentResponse;
import com.blogapi.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/blog/comments")
public class CommentController {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    @Autowired
    private CommentService commentService;
    
    @PostMapping("/posts/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        logger.info("Creating comment for post: {}", postId);
        CommentResponse response = commentService.createComment(postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        logger.info("Getting comments for post: {}", postId);
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
        logger.info("Getting comment by id: {}", commentId);
        CommentResponse comment = commentService.getCommentById(commentId);
        return ResponseEntity.ok(comment);
    }
    
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        logger.info("Updating comment: {}", commentId);
        CommentResponse response = commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        logger.info("Deleting comment: {}", commentId);
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByUserId(@PathVariable Long userId) {
        logger.info("Getting comments by user: {}", userId);
        List<CommentResponse> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }
}
