package com.blogapi.service.Implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogapi.config.SecurityConfig;
import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.Comment;
import com.blogapi.model.Post;
import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.User;
import com.blogapi.payload.CommentRequest;
import com.blogapi.payload.CommentResponse;
import com.blogapi.repository.CommentRepository;
import com.blogapi.repository.PostRepository;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUser() != null ? comment.getUser().getUsername() : null,
                comment.getPost() != null ? comment.getPost().getId() : null
        );
    }
    
    @Override
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest commentRequest) {
        logger.info("Creating comment for post: {}", postId);
        
        // Find the post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        
        // Get current authenticated user
        String currentUsername = SecurityConfig.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        
        // Create the comment
        Comment comment = Comment.builder()
                .body(commentRequest.body())
                .user(currentUser)
                .post(post)
                .build();
        
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment created successfully with id: {}", savedComment.getId());
        
        return mapToResponse(savedComment);
    }
    
    @Override
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        logger.info("Updating comment: {}", commentId);
        
        Comment comment = commentRepository.findByIdWithDetails(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        // Check if current user is the author of the comment or an admin
        String currentUsername = SecurityConfig.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        
        if (!comment.getUser().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You are not authorized to update this comment.");
        }
        
        comment.setBody(commentRequest.body());
        Comment updatedComment = commentRepository.save(comment);
        
        logger.info("Comment updated successfully: {}", commentId);
        return mapToResponse(updatedComment);
    }
    
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        logger.info("Deleting comment: {}", commentId);
        
        Comment comment = commentRepository.findByIdWithDetails(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        // Check if current user is the author of the comment, post author, or an admin
        String currentUsername = SecurityConfig.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        
        boolean isCommentAuthor = comment.getUser().getId().equals(currentUser.getId());
        boolean isPostAuthor = comment.getPost().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == ROLE_TYPE.ADMIN;

        
        if (!isCommentAuthor && !isPostAuthor && !isAdmin) {
            throw new AccessDeniedException("You are not authorized to delete this comment.");
        }
        
        commentRepository.delete(comment);
        logger.info("Comment deleted successfully: {}", commentId);
    }
    
    @Override
    public CommentResponse getCommentById(Long commentId) {
        logger.info("Getting comment by id: {}", commentId);
        
        Comment comment = commentRepository.findByIdWithDetails(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        
        return mapToResponse(comment);
    }
    
    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        logger.info("Getting comments for post: {}", postId);
        
        // Verify post exists
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
        logger.info("Found {} comments for post: {}", comments.size(), postId);
        
        return comments.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public List<CommentResponse> getCommentsByUserId(Long userId) {
        logger.info("Getting comments by user: {}", userId);
        
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        
        List<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        logger.info("Found {} comments by user: {}", comments.size(), userId);
        
        return comments.stream()
                .map(this::mapToResponse)
                .toList();
    }
} 