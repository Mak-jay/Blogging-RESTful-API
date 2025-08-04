package com.blogapi.service.Implementation;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blogapi.config.SecurityConfig;
import com.blogapi.exception.ResourceNotFoundException;
import com.blogapi.exception.UnauthorizedException;
import com.blogapi.exception.UserNotFoundException;
import com.blogapi.model.Category;
import com.blogapi.model.Post;
import com.blogapi.model.ROLE_TYPE;
import com.blogapi.model.Tag;
import com.blogapi.model.User;
import com.blogapi.payload.PostRequest;
import com.blogapi.payload.PostResponse;
import com.blogapi.repository.CategoryRepository;
import com.blogapi.repository.PostRepository;
import com.blogapi.repository.TagRepository;
import com.blogapi.repository.UserRepository;
import com.blogapi.service.PostService;

@Service
public class PostServiceImpl implements PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

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

        // Get comment count
        Integer commentCount = post.getComments() != null ? post.getComments().size() : 0;

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
                tagNames,
                commentCount
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
        String username = SecurityConfig.getCurrentUsername();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (currentUser.getRole() == ROLE_TYPE.AUTHOR && !currentUser.isEnabled()) {
            throw new UnauthorizedException("Author not approved yet");
        }

        Post post = new Post();
        post.setTitle(postRequest.title());
        post.setSlug(generateSlug(postRequest.title()));
        post.setContent(postRequest.content());
        post.setPublished(postRequest.published());
        String user = SecurityConfig.getCurrentUsername();
        User currUser = userRepository.findByUsername(user)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        post.setUser(currUser);
        post.setCategory(getCategory(postRequest.categoryId()));
        post.setTags(getTags(postRequest.tagIds()));


        return mapToResponse(postRepository.save(post));

    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findByIdWithDetails(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post with id "+id+" not found"));
        return mapToResponse(post);
    }



    //Either Author of the post or Admin can delete the posts
    @Override
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Set<Tag> tags = new HashSet<>(tagRepository.findAllById(request.tagIds()));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (!existingPost.getUser().getId().equals(currentUser.getId())
                && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You are not authorized to update this post.");
        }


        existingPost.setTitle(request.title());
        existingPost.setSlug(generateSlug(request.title())); // Optional: regenerate slug
        existingPost.setContent(request.content());
        existingPost.setPublished(request.published());
        existingPost.setUpdatedAt(LocalDateTime.now());
        String username = SecurityConfig.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                        .orElseThrow(()-> new UserNotFoundException("User "+username+" not found"));
        existingPost.setCategory(category);
        Set<Tag> updatedTags = new HashSet<>(tags); // or List, depends on your mapping
        existingPost.setTags(updatedTags);

        existingPost.setUser(user); // Update if needed, or keep original

        Post updated = postRepository.save(existingPost);

        return mapToResponse(updated);
    }

    //Either Author of the post or Admin can delete the posts
    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new UserNotFoundException("Post with id " + id + " not found"));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        if (!post.getUser().getId().equals(currentUser.getId())
                && !currentUser.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You are not authorized to delete this post.");
        }
        postRepository.deleteAllByPostId(id);
        
        postRepository.delete(post);
    }

    @Override
    public PostResponse getPostBySlug(String slugName) {
        Post post = postRepository.findBySlugWithDetails(slugName)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return mapToResponse(post);
    }
    @Override
    public List<PostResponse> searchPostsByTitleOrContent(String query) {
        logger.info("Searching posts by title or content with query: {}", query);
        List<Post> posts = postRepository.searchByTitleOrContentWithJoins(query);
        logger.info("Found {} posts matching query: {}", posts.size(), query);
        return posts.stream()
               .map(this::mapToResponse)
               .toList();
    }

@Override
public List<PostResponse> searchPostsByCategory(String categoryName) {
    logger.info("Searching posts by category: {}", categoryName);
    List<Post> posts = postRepository.findPostsByCategoryName(categoryName);
    logger.info("Found {} posts in category: {}", posts.size(), categoryName);
    return posts.stream()
           .map(this::mapToResponse)
           .toList();
}

@Override
public List<PostResponse> searchPostsByTag(String tagName) {
    logger.info("Searching posts by tag: {}", tagName);
    List<Post> posts = postRepository.findPostsByTagName(tagName);
    logger.info("Found {} posts with tag: {}", posts.size(), tagName);
    return posts.stream()
           .map(this::mapToResponse)
           .toList(); 
}

@Override
public List<PostResponse> searchPostsByAuthor(String authorName) {
    logger.info("Searching posts by author: {}", authorName);
    List<Post> posts = postRepository.findByAuthorUsername(authorName);
    logger.info("Found {} posts by author: {}", posts.size(), authorName);
    return posts.stream()
           .map(this::mapToResponse)
           .toList();
}

@Override
public List<PostResponse> searchPostsCombined(String query, String category, String tag, String author) {
    logger.info("Searching posts with combined criteria - query: {}, category: {}, tag: {}, author: {}", 
                query, category, tag, author);
    List<Post> posts = postRepository.findPostsByCombinedCriteria(query, category, tag, author);
    logger.info("Found {} posts with combined criteria", posts.size());
    return posts.stream()
           .map(this::mapToResponse)
           .toList();
}

}

