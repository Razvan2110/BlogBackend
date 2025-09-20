//CREATED AT: 15/9/2025
//BY: UNGUREANU RAZVAN
//TEST CLASS FOR PostService

package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost_setsTimestampsAndSaves() {
        Post post = new Post("Test Title", "Test Content", "Test Author");

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post savedPost = postService.createPost(post);

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getCreatedAt()).isNotNull();
        assertThat(savedPost.getUpdatedAt()).isNotNull();
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetAllPosts_returnsList() {
        List<Post> posts = Arrays.asList(
                new Post("Title1", "Content1", "Author1"),
                new Post("Title2", "Content2", "Author2")
        );

        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Title1");
    }

    @Test
    void testGetPostById_found() {
        Post post = new Post("Title", "Content", "Author");
        when(postRepository.findById("1")).thenReturn(Optional.of(post));

        Optional<Post> result = postService.getPostById("1");

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Title");
    }

    @Test
    void testGetPostById_notFound() {
        when(postRepository.findById("99")).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPostById("99");

        assertThat(result).isEmpty();
    }

    @Test
    void testUpdatePost_success() {
        Post existingPost = new Post("Old Title", "Old Content", "Old Author");
        Post updatedPost = new Post("New Title", "New Content", "New Author");

        when(postRepository.findById("1")).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        Post result = postService.updatePost("1", updatedPost);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getContent()).isEqualTo("New Content");
        assertThat(result.getAuthor()).isEqualTo("New Author");
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    void testUpdatePost_notFound_throwsException() {
        Post updatedPost = new Post("New Title", "New Content", "New Author");

        when(postRepository.findById("404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost("404", updatedPost))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Post not found");
    }

    @Test
    void testDeletePost_callsRepository() {
        doNothing().when(postRepository).deleteById("1");

        postService.deletePost("1");

        verify(postRepository, times(1)).deleteById("1");
    }
}
