package com.blogapi.service;

import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<Tag> getAllTags();

    void createTag(@Valid TagRequest tagRequest);

    Tag getTagById(Long id);

    void deleteTagById(Long id);
}
