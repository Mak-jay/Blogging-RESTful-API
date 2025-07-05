package com.blogapi.service.Implementation;

import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import com.blogapi.payload.TagResponse;
import com.blogapi.repository.TagRepository;
import com.blogapi.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagResponse mapToResponse(Tag tag){
        return new TagResponse(tag.getId(),tag.getName());
    }

    @Override
    public List<TagResponse> getAllTags() {
    return tagRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Override
    public void createTag(TagRequest tagRequest) {
        Tag newTag = new Tag();
        newTag.setName(tagRequest.name());
        tagRepository.save(newTag);
    }

    @Override
    public TagResponse getTagById(Long id) {
        return tagRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(()-> new ResourceNotFoundException("Tag is not found"));
    }

    @Override
    public void deleteTagById(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        tagRepository.deleteById(id);
    }


}
