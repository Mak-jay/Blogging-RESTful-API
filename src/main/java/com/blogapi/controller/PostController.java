package com.blogapi.controller;

import com.blogapi.model.Post;
import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;
import com.blogapi.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostResponse>> getAllPosts()
    {
        return ResponseEntity.ok(postService.getAllPosts());
    }


}
