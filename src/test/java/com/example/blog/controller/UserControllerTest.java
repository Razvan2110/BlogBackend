package com.example.blog.controller;

import com.example.blog.model.Role;
import com.example.blog.model.User;
import com.example.blog.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        user1 = new User("john", "password", "john@example.com", "1234567890", Role.VIEWER);
        user1.setId("1");

        user2 = new User("admin", "adminpass", "admin@example.com", "0987654321", Role.ADMIN);
        user2.setId("2");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        given(userService.getAllUsers()).willReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("john"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
    }

    @Test
    public void testGetUserById_found() throws Exception {
        given(userService.getUserById("1")).willReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testGetUserById_notFound() throws Exception {
        given(userService.getUserById("99")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        User newUser = new User("newuser", "newpass", "newuser@example.com", "111222333", Role.VIEWER);
        User savedUser = new User("newuser", "encodedpass", "newuser@example.com", "111222333", Role.VIEWER);
        savedUser.setId("3");

        given(userService.createUser(ArgumentMatchers.any(User.class))).willReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    public void testUpdateUser_found() throws Exception {
        User updatedUser = new User("john_updated", "newpass", "john_updated@example.com", "1234567890", Role.ADMIN);
        updatedUser.setId("1");

        given(userService.updateUser(ArgumentMatchers.eq("1"), ArgumentMatchers.any(User.class)))
                .willReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john_updated"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void testUpdateUser_notFound() throws Exception {
        User updatedUser = new User("john_updated", "newpass", "john_updated@example.com", "1234567890", Role.ADMIN);
        updatedUser.setId("99");

        Mockito.when(userService.updateUser(ArgumentMatchers.eq("99"), ArgumentMatchers.any(User.class)))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/api/users/99")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser("1");

        mockMvc.perform(delete("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
