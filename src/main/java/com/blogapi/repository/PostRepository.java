package com.blogapi.repository;

import com.blogapi.model.Post;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsBySlug(String slug);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.tags " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.user")
    List<Post> findAllWithTagsAndCategoryAndUser();
}
