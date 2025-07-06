package com.blogapi.service;

import com.blogapi.model.Post;
import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    List<PostResponse> getAllPosts();

    PostResponse createPost(@Valid PostRequest postRequest);

    PostResponse getPostById(Long id);

    PostResponse updatePost(Long id,@Valid PostRequest postRequest);

    void deletePost(Long id);
}
