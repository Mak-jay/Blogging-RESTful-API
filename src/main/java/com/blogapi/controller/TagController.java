package com.blogapi.controller;

import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import com.blogapi.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody @Valid TagRequest tagRequest){
        tagService.createTag(tagRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAll(){
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(Collections.singletonList((Tag) tags));
    }

    @GetMapping("{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id){
        Tag tag =tagService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteTag(Long id){
        tagService.deleteTagById(id);
        return ResponseEntity.ok().build();
    }





}
