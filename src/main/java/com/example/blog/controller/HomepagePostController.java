package com.example.blog.controller;

import com.example.blog.model.HomepagePost;
import com.example.blog.service.HomepagePostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homepage")
public class HomepagePostController {
    private final HomepagePostService service;

    public HomepagePostController(HomepagePostService service) {
        this.service = service;
    }

    @GetMapping
    public List<HomepagePost> getAllHomepagePosts() {
        return service.getAllHomepagePosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomepagePost> getHomepagePostById(@PathVariable String id) {
        return service.getHomepagePostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HomepagePost> createHomepagePost(@RequestBody HomepagePost post) {
        return ResponseEntity.ok(service.createHomepagePost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomepagePost> updateHomepagePost(
            @PathVariable String id,
            @RequestBody HomepagePost post
    ) {
        try {
            return ResponseEntity.ok(service.updateHomepagePost(id, post));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomepagePost(@PathVariable String id) {
        service.deleteHomepagePost(id);
        return ResponseEntity.noContent().build();
    }
}
