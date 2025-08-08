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

import com.blogapi.model.Category;
import com.blogapi.payload.CategoryRequest;
import com.blogapi.payload.CategoryResponse;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.service.Implementation.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category testCategory;
    private CategoryRequest testCategoryRequest;

    @BeforeEach
    void setUp() {
        // Setup test category
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Technology");

        // Setup test category request
        testCategoryRequest = new CategoryRequest("Technology");
    }

    @Test
    void createCategory_Success() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        // Act
        CategoryResponse response = categoryService.createCategory(testCategoryRequest);

        // Assert
        assertNotNull(response);
        assertEquals(testCategory.getName(), response.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getCategoryById_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act
        CategoryResponse response = categoryService.getCategoryById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(testCategory.getName(), response.name());
    }

    @Test
    void getCategoryById_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.getCategoryById(999L));
    }

    @Test
    void getAllCategories_Success() {
        // Arrange
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        // Act
        List<CategoryResponse> response = categoryService.getAllCategories();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testCategory.getName(), response.get(0).name());
    }

    @Test
    void deleteCategory_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        doNothing().when(categoryRepository).delete(testCategory);

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository).delete(testCategory);
    }

    @Test
    void deleteCategory_NotFound() {
        // Arrange
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(999L));
    }
}
