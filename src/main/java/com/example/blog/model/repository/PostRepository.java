//CREATED AT:13/9/2025
//BY: UNGUREANU RAZVAN
//CLASS FOR MANAGING POSTS



package com.example.blog.model.repository;

import com.example.blog.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    // Pentru început nu e nevoie de metode custom
}
