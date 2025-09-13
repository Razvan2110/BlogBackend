package com.example.blog.model.repository;

import com.example.blog.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

//import java.lang.ScopedValue;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
