package com.blogapi.controller;

import com.blogapi.model.Post;
import com.blogapi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/blog/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/test")
    public ResponseEntity<String> testAccess() {
        return ResponseEntity.ok("You have accessed");
    }


    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts()
    {
        List<Post> allPosts = postService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }
//    @PostMapping
//    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
//        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest));
//    }

}
