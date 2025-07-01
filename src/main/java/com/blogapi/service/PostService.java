package com.blogapi.service;

import com.blogapi.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    List<Post> getAllPosts();

//    PostResponse createPost(@Valid PostRequest postRequest);
}
