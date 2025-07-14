package com.blogapi.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;
import com.blogapi.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/blog/posts")
public class PostController {

    @Autowired
    private PostService postService;


    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts()
    {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getPostsById(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/slug")
    public ResponseEntity<PostResponse> getPostBySlug(@RequestParam("slug") String slugName){
        return ResponseEntity.ok(postService.getPostBySlug(slugName));
    }
    
    @GetMapping("/search")
    public ResponseEntity<Object> searchPosts(
            @RequestParam("query") String query
    ) {
        return ResponseEntity.ok(postService.searchPostsByTitleOrContent(query));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id,@Valid @RequestBody PostRequest postRequest){
        return ResponseEntity.ok(postService.updatePost(id,postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
         postService.deletePost(id);
         return ResponseEntity.noContent().build();
    }



}
