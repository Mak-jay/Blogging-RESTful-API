package com.blogapi.serviceTests;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.blogapi.model.Comment;
import com.blogapi.model.Post;
import com.blogapi.model.User;
import com.blogapi.payload.CommentRequest;
import com.blogapi.payload.CommentResponse;
import com.blogapi.repository.CommentRepository;
import com.blogapi.repository.PostRepository;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.Implementation.CommentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
    private CommentRequest testCommentRequest;
    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test post
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test post content");

        // Setup test comment
        testComment = new Comment();
        testComment.setId(1L);
        testComment.setBody("This is a test comment");
        testComment.setUser(testUser);
        testComment.setPost(testPost);
        testComment.setCreatedAt(LocalDateTime.now());
        
        // Ensure testPost has a user
        testPost.setUser(testUser);

        // Setup test comment request
        testCommentRequest = new CommentRequest("This is a test comment");
    }

    @Test
    void createComment_Success() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(userRepository.findByUsername(null)).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        CommentResponse response = commentService.createComment(1L, testCommentRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testComment.getBody(), response.body());
        assertEquals(testComment.getUser().getUsername(), response.authorUsername());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_PostNotFound() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            commentService.createComment(999L, testCommentRequest));
    }

    @Test
    void getCommentById_Success() {
        // Arrange
        when(commentRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testComment));

        // Act
        CommentResponse response = commentService.getCommentById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(testComment.getBody(), response.body());
        assertEquals(testComment.getUser().getUsername(), response.authorUsername());
    }

    @Test
    void getCommentById_NotFound() {
        // Arrange
        lenient().when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> commentService.getCommentById(999L));
    }

    @Test
    void getCommentsByPost_Success() {
        // Arrange
        List<Comment> comments = Arrays.asList(testComment);
        when(postRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findByPostIdOrderByCreatedAtDesc(1L)).thenReturn(comments);

        // Act
        List<CommentResponse> response = commentService.getCommentsByPostId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testComment.getBody(), response.get(0).body());
    }

    @Test
    void updateComment_Success() {
        // Arrange
        CommentRequest updateRequest = new CommentRequest("Updated comment content");
        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setBody("Updated comment content");
        updatedComment.setUser(testUser);
        updatedComment.setPost(testPost);

        when(commentRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testComment));
        when(userRepository.findByUsername(null)).thenReturn(Optional.of(testUser));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        // Act
        CommentResponse response = commentService.updateComment(1L, updateRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Updated comment content", response.body());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_NotFound() {
        // Arrange
        lenient().when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            commentService.updateComment(999L, testCommentRequest));
    }

    @Test
    void deleteComment_Success() {
        // Arrange
        when(commentRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(testComment));
        when(userRepository.findByUsername(null)).thenReturn(Optional.of(testUser));
        doNothing().when(commentRepository).delete(testComment);

        // Act
        commentService.deleteComment(1L);

        // Assert
        verify(commentRepository).delete(testComment);
    }

    @Test
    void deleteComment_NotFound() {
        // Arrange
        lenient().when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            commentService.deleteComment(999L));
    }

    @Test
    void getCommentsByUser_Success() {
        // Arrange
        List<Comment> comments = Arrays.asList(testComment);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(comments);

        // Act
        List<CommentResponse> response = commentService.getCommentsByUserId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testComment.getBody(), response.get(0).body());
    }
}
