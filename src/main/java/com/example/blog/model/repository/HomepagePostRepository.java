package com.example.blog.model.repository;

import com.example.blog.model.HomepagePost;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HomepagePostRepository extends MongoRepository<HomepagePost, String> {
    // momentan nu ai nevoie de metode custom
}
