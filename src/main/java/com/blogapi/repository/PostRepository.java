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
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.tags LEFT JOIN FETCH p.category WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchByTitleOrContentWithJoins(@Param("query") String query);

    //List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String query, String query2);
}
