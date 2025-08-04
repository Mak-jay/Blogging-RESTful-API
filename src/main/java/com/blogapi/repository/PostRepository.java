package com.blogapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blogapi.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsBySlug(String slug);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.tags " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.user")
    List<Post> findAllWithTagsAndCategoryAndUser();

    @Query("""
    SELECT p FROM Post p
    LEFT JOIN FETCH p.tags
    LEFT JOIN FETCH p.category
    LEFT JOIN FETCH p.user
    WHERE p.id = :id""")
    Optional<Post> findByIdWithDetails(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post_tags WHERE post_id = :postId", nativeQuery = true)
    void deleteAllByPostId(@Param("postId") Long postId);


    @Query("""
    SELECT p FROM Post p
    LEFT JOIN FETCH p.tags
    LEFT JOIN FETCH p.category
    LEFT JOIN FETCH p.user
    WHERE p.slug = :slug""")
    Optional<Post> findBySlugWithDetails(@Param("slug") String slugName);

    // In PostRepository.java
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.category LEFT JOIN FETCH p.user WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchByTitleOrContentWithJoins(@Param("query") String query);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.category LEFT JOIN FETCH p.user JOIN p.category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))")
    List<Post> findPostsByCategoryName(@Param("categoryName") String categoryName);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.category LEFT JOIN FETCH p.user JOIN p.tags t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%'))")
    List<Post> findPostsByTagName(@Param("tagName") String tagName);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.category LEFT JOIN FETCH p.user WHERE LOWER(p.user.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<Post> findByAuthorUsername(@Param("username") String username);

    @Query("""
        SELECT DISTINCT p FROM Post p 
        LEFT JOIN FETCH p.tags 
        LEFT JOIN FETCH p.category 
        LEFT JOIN FETCH p.user 
        WHERE (:query IS NULL OR (LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%'))))
        AND (:category IS NULL OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%')))
        AND (:tag IS NULL OR EXISTS (SELECT 1 FROM p.tags t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :tag, '%'))))
        AND (:author IS NULL OR LOWER(p.user.username) LIKE LOWER(CONCAT('%', :author, '%')))
        """)
    List<Post> findPostsByCombinedCriteria(
        @Param("query") String query,
        @Param("category") String category,
        @Param("tag") String tag,
        @Param("author") String author
    );
}
