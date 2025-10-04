package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "posts")
@JsonPropertyOrder({ "id", "title", "content", "author", "images", "createdAt", "updatedAt" })
public class Post {

    @Id
    @JsonProperty("_id")
    private String id;

    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @Field("author")
    private String author;

    @Field("images")
    private List<String> images; // poate fi null sau gol

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    public Post() {}

    public Post(String title, String content, String author, List<String> images) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.images = images;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // getters și setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public List<String> getImages() { return images; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setAuthor(String author) { this.author = author; }
    public void setImages(List<String> images) { this.images = images; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
