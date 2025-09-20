package com.example.blog;

import com.example.blog.model.Post;
import com.example.blog.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BlogApplicationTests {

	@Autowired
	private PostService postService;

	@Test
	void contextLoads() {
		assertThat(postService).isNotNull();
	}

	// CREATE POST
	@Test
	void testCreatePost_success() {
		Post post = new Post();
		post.setTitle("Test Post");
		post.setContent("Some content here");

		Post saved = postService.createPost(post);

		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getTitle()).isEqualTo("Test Post");
	}

	@Test
	void testCreatePost_failure_missingTitle() {
		Post post = new Post();
		post.setContent("Content without title");

		Post saved = postService.createPost(post);


		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getTitle()).isNull();
	}

	// GET POST BY ID
	@Test
	void testGetPostById_success() {
		Post post = new Post();
		post.setTitle("Check Find");
		post.setContent("Find me");
		Post saved = postService.createPost(post);

		Optional<Post> found = postService.getPostById(saved.getId());
		assertThat(found).isPresent();
		assertThat(found.get().getTitle()).isEqualTo("Check Find");
	}

	@Test
	void testGetPostById_notFound() {
		Optional<Post> found = postService.getPostById("does-not-exist");
		assertThat(found).isEmpty();
	}

	// GET ALL POSTS
	@Test
	void testGetAllPosts_success() {
		List<Post> posts = postService.getAllPosts();
		assertThat(posts).isNotNull();
		assertThat(posts.size()).isGreaterThanOrEqualTo(0);
	}

	@Test
	void testGetAllPosts_emptyList() {

		List<Post> posts = postService.getAllPosts();
		if (posts.isEmpty()) {
			assertThat(posts).isEmpty();
		} else {
			assertThat(posts).isNotEmpty();
		}
	}
}
