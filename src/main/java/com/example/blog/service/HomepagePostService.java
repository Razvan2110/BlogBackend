package com.example.blog.service;

import com.example.blog.model.HomepagePost;
import com.example.blog.model.repository.HomepagePostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HomepagePostService {
    private final HomepagePostRepository repository;

    public HomepagePostService(HomepagePostRepository repository) {
        this.repository = repository;
    }

    public HomepagePost createHomepagePost(HomepagePost post) {
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        return repository.save(post);
    }

    public List<HomepagePost> getAllHomepagePosts() {
        return repository.findAll();
    }

    public Optional<HomepagePost> getHomepagePostById(String id) {
        return repository.findById(id);
    }

    public HomepagePost updateHomepagePost(String id, HomepagePost updatedPost) {
        HomepagePost post = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HomepagePost not found"));

        post.setAuthor(updatedPost.getAuthor());
        post.setContent(updatedPost.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        return repository.save(post);
    }

    public void deleteHomepagePost(String id) {
        repository.deleteById(id);
    }
}
