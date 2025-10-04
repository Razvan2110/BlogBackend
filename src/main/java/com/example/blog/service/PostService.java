package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        if (post.getImages() == null) post.setImages(new ArrayList<>());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Post updatePost(String id, Post updatedPost) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setAuthor(updatedPost.getAuthor());
        post.setUpdatedAt(LocalDateTime.now());
        if (updatedPost.getImages() != null) {
            post.setImages(updatedPost.getImages());
        }
        return postRepository.save(post);
    }

    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    // Adaugă o imagine la o postare existentă
    public Post addImageToPost(String id, String imageUrl) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        List<String> images = post.getImages();
        if (images == null) images = new ArrayList<>();
        images.add(imageUrl);
        post.setImages(images);

        return postRepository.save(post);
    }
}
