package com.blogapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;

import jakarta.validation.Valid;

@Service
public interface PostService {
    List<PostResponse> getAllPosts();

    PostResponse createPost(@Valid PostRequest postRequest);

    PostResponse getPostById(Long id);

    PostResponse updatePost(Long id,@Valid PostRequest postRequest);

    void deletePost(Long id);

    PostResponse getPostBySlug(String slugName);

    List<PostResponse> searchPostsByTitleOrContent(String query);

    List<PostResponse> searchPostsByCategory(String categoryName);

    List<PostResponse> searchPostsByTag(String tagName);

    List<PostResponse> searchPostsByAuthor(String authorName);

    List<PostResponse> searchPostsCombined(String query, String category, String tag, String author);
}
