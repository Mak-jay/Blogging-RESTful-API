package com.blogapi.service.Implementation;

import com.blogapi.model.Post;
import com.blogapi.repository.PostRepository;
import com.blogapi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

//    @Override
//    public PostResponse createPost(PostRequest postRequest) {
//        Post newPost = new Post();
//
////        newPost.se
//    }


}
