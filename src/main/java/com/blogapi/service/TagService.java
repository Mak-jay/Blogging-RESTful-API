package com.blogapi.service;

import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import com.blogapi.payload.TagResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<TagResponse> getAllTags();

    void createTag(@Valid TagRequest tagRequest);

    TagResponse getTagById(Long id);

    void deleteTagById(Long id);
}
