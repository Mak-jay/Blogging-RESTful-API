package com.blogapi.serviceTests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.blogapi.model.Tag;
import com.blogapi.payload.TagRequest;
import com.blogapi.payload.TagResponse;
import com.blogapi.repository.TagRepository;
import com.blogapi.service.Implementation.TagServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag testTag;
    private TagRequest testTagRequest;

    @BeforeEach
    void setUp() {
        // Setup test tag
        testTag = new Tag();
        testTag.setId(1L);
        testTag.setName("Java");

        // Setup test tag request
        testTagRequest = new TagRequest("Java");
    }

    @Test
    void createTag_Success() {
        // Arrange
        when(tagRepository.save(any(Tag.class))).thenReturn(testTag);

        // Act
        tagService.createTag(testTagRequest);

        // Assert
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void getTagById_Success() {
        // Arrange
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));

        // Act
        TagResponse response = tagService.getTagById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(testTag.getName(), response.name());
    }

    @Test
    void getTagById_NotFound() {
        // Arrange
        when(tagRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tagService.getTagById(999L));
    }

    @Test
    void getAllTags_Success() {
        // Arrange
        List<Tag> tags = Arrays.asList(testTag);
        when(tagRepository.findAll()).thenReturn(tags);

        // Act
        List<TagResponse> response = tagService.getAllTags();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testTag.getName(), response.get(0).name());
    }

    @Test
    void deleteTag_Success() {
        // Arrange
        when(tagRepository.findById(1L)).thenReturn(Optional.of(testTag));
        doNothing().when(tagRepository).deleteById(1L);

        // Act
        tagService.deleteTagById(1L);

        // Assert
        verify(tagRepository).deleteById(1L);
    }

    @Test
    void deleteTag_NotFound() {
        // Arrange
        when(tagRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tagService.deleteTagById(999L));
    }
}
