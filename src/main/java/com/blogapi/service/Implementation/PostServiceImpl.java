package com.blogapi.service.Implementation;

import com.blogapi.config.SecurityConfig;
import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.exception.UserNotFoundException;
import com.blogapi.model.Category;
import com.blogapi.model.Post;
import com.blogapi.model.Tag;
import com.blogapi.model.User;
import com.blogapi.payload.CategoryResponse;
import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;
import com.blogapi.payload.TagResponse;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.repository.PostRepository;
import com.blogapi.repository.TagRepository;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.CategoryService;
import com.blogapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    public String generateSlug(String title)
    {
        String baseSlug = title
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")   // Remove special characters
                .replaceAll("\\s+", "-")          // Replace whitespace with hyphen
                .replaceAll("-{2,}", "-")         // Replace multiple hyphens with one
                .replaceAll("^-|-$", "");         // Trim starting/ending hyphens

        String slug = baseSlug;
        int counter = 1;

        while (postRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
    private PostResponse mapToResponse(Post post) {
        Set<String> tagNames = post.getTags() != null
                ? post.getTags()
                .stream()
                .map(Tag::getName)
                .collect(Collectors.toSet())
                : Set.of(); // avoid nulls

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getContent(),
                post.getPublished(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser() != null ? post.getUser().getUsername() : null,
                post.getCategory() != null ? post.getCategory().getName() : null,
                tagNames
        );
    }




    //extract category from id
    public Category getCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    //extract tags from id
    public Set<Tag> getTags(Set<Long> ids){
        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(ids));
        if (tags.size() != ids.size()) {
            throw new IllegalArgumentException("One or more tag IDs are invalid");
        }
        return tags;
    }


    @Override
    public List<PostResponse> getAllPosts() {
        List<Post> postList= postRepository.findAllWithTagsAndCategoryAndUser();
        return postList.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        Post post = new Post();
        post.setTitle(postRequest.title());
        post.setSlug(generateSlug(postRequest.title()));
        post.setContent(postRequest.content());
        post.setPublished(postRequest.published());
        String username = SecurityConfig.getCurrentUsername();
        User currentUser = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        post.setUser(currentUser);
        post.setCategory(getCategory(postRequest.categoryId()));
        post.setTags(getTags(postRequest.tagIds()));


        return mapToResponse(postRepository.save(post));

    }


}

