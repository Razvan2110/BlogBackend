//CREATED AT:13/9/2025
//BY: UNGUREANU RAZVAN
//CLASS FOR MANAGING POSTS



package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post) {
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        return postRepository.save(post);
    }


    // Returnează toate postările
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Returnează o postare după ID
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    // Update postare după ID
    public Post updatePost(String id, Post updatedPost) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setAuthor(updatedPost.getAuthor());
        post.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(post);
    }


    // Șterge postare după ID
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }
}
