package com.blogapi.controller;

import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import com.blogapi.payload.TagResponse;
import com.blogapi.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/blog/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody @Valid TagRequest tagRequest){
        tagService.createTag(tagRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public List<TagResponse> getAll(){
        return tagService.getAllTags();
    }

    @GetMapping("{id}")
    public ResponseEntity<TagResponse> getTagById(@PathVariable Long id){
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTag(Long id){
        tagService.deleteTagById(id);
        return ResponseEntity.ok().build();
    }





}
