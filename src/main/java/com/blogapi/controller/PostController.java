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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;
import com.blogapi.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/blog/posts")
public class PostController {

     private static final Logger logger = LoggerFactory.getLogger(PostController.class);

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
    public ResponseEntity<List<PostResponse>> searchPosts(
            @RequestParam("query") String query
    ) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(postService.searchPostsByTitleOrContent(query.trim()));
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<PostResponse>> searchPostsByCategory(
            @RequestParam("category") String categoryName
    ) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        logger.info("Searching posts by category: {}", categoryName);
        return ResponseEntity.ok(postService.searchPostsByCategory(categoryName.trim()));
    }

    @GetMapping("/search/tag")
    public ResponseEntity<List<PostResponse>> searchPostsByTag(
            @RequestParam("tag") String tagName
    ) {
        if (tagName == null || tagName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(postService.searchPostsByTag(tagName.trim()));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<PostResponse>> searchPostsByAuthor(
            @RequestParam("author") String authorName
    ) {
        if (authorName == null || authorName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(postService.searchPostsByAuthor(authorName.trim()));
    }

    @GetMapping("/search/combined")
    public ResponseEntity<List<PostResponse>> searchPostsCombined(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "author", required = false) String author
    ) {
        // At least one search parameter must be provided
        if ((query == null || query.trim().isEmpty()) &&
            (category == null || category.trim().isEmpty()) &&
            (tag == null || tag.trim().isEmpty()) &&
            (author == null || author.trim().isEmpty())) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(postService.searchPostsCombined(
            query != null ? query.trim() : null,
            category != null ? category.trim() : null,
            tag != null ? tag.trim() : null,
            author != null ? author.trim() : null
        ));
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