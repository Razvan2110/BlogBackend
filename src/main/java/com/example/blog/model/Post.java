//CREATED AT:13/9/2025
//BY: UNGUREANU RAZVAN
//CLASS FOR MANAGING POSTS
/**
 * Represents a blog post stored in the MongoDB collection "posts".
 * <p>
 * Each post has a title, content, author, and timestamps for creation and update.
 * This class is annotated as a MongoDB document and is used by Spring Data.
 * </p>
 *
 * @author Ungureanu
 * @since 2025-09-13
 */

package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "posts")

@JsonPropertyOrder({ "id", "title", "content", "author", "createdAt", "updatedAt" })
public class Post {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("author")
    private String author; // sau User author;

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    // Constructor, getter, setter
    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
