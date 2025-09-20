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

    private User adminUser1;
    private User adminUser2;

    @BeforeEach
    public void setup() {
        adminUser1 = new User("admin1", "password", "admin1@example.com", "1234567890", Role.ADMIN);
        adminUser1.setId("1");

        adminUser2 = new User("admin2", "adminpass", "admin2@example.com", "0987654321", Role.ADMIN);
        adminUser2.setId("2");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        given(userService.getAllUsers()).willReturn(Arrays.asList(adminUser1, adminUser2));

        mockMvc.perform(get("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin1"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
    }

    @Test
    public void testGetUserById_found() throws Exception {
        given(userService.getUserById("1")).willReturn(Optional.of(adminUser1));

        mockMvc.perform(get("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin1"))
                .andExpect(jsonPath("$.email").value("admin1@example.com"));
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
        User newAdmin = new User("newadmin", "newpass", "newadmin@example.com", "111222333", Role.ADMIN);
        User savedAdmin = new User("newadmin", "newpass", "newadmin@example.com", "111222333", Role.ADMIN);
        savedAdmin.setId("3"); // ID generat

        given(userService.createUser(ArgumentMatchers.any(User.class))).willReturn(savedAdmin);


        mockMvc.perform(post("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.username").value("newadmin"));
    }

    @Test
    public void testUpdateUser_found() throws Exception {
        User updatedAdmin = new User("admin1_updated", "newpass", "admin1_updated@example.com", "1234567890", Role.ADMIN);
        updatedAdmin.setId("1");

        given(userService.updateUser(ArgumentMatchers.eq("1"), ArgumentMatchers.any(User.class)))
                .willReturn(updatedAdmin);

        mockMvc.perform(put("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin1_updated"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void testUpdateUser_notFound() throws Exception {
        User updatedAdmin = new User("admin99", "newpass", "admin99@example.com", "1234567890", Role.ADMIN);
        updatedAdmin.setId("99");

        Mockito.when(userService.updateUser(ArgumentMatchers.eq("99"), ArgumentMatchers.any(User.class)))
                .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(put("/api/users/99")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAdmin)))
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

    @Test
    public void testGetAllUsers_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized()); // sau 401
    }

    @Test
    public void testGetUserById_unauthenticated() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateUser_unauthenticated() throws Exception {
        User newUser = new User("newuser", "newpass", "newuser@example.com", "111222333", Role.ADMIN);
        mockMvc.perform(post("/api/users")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // csrf e necesar la POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUpdateUser_unauthenticated() throws Exception {
        User updatedUser = new User("john_updated", "newpass", "john_updated@example.com", "1234567890", Role.ADMIN);
        mockMvc.perform(put("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testDeleteUser_unauthenticated() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isUnauthorized());
    }

}
