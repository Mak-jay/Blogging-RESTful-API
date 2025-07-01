package com.blogapi.service.Implementation;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import com.blogapi.repository.TagRepository;
import com.blogapi.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public void createTag(TagRequest tagRequest) {
        Tag newTag = new Tag();
        newTag.setName(tagRequest.name());
        tagRepository.save(newTag);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        tagRepository.deleteById(id);
    }


}
