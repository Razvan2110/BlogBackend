
package com.example.blog.service;

import com.example.blog.model.Role;
import com.example.blog.model.User;
import com.example.blog.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        User user = new User("testuser", "plainPassword", "test@example.com", "123456", Role.VIEWER);

        // când se apelează save, să întoarcă chiar userul primit (mock behavior)
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(user);

        // verifică că parola a fost criptată (nu e egală cu textul simplu)
        assertNotEquals("plainPassword", createdUser.getPassword());

        // verifică că parola criptată e validă (folosind BCrypt)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("plainPassword", createdUser.getPassword()));

        // verifică că userRepository.save a fost apelat o singură dată
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById_ShouldReturnUserIfExists() {
        User user = new User("testuser", "password", "test@example.com", "123456", Role.ADMIN);
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById("1");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void getUserById_ShouldReturnEmptyIfNotFound() {
        when(userRepository.findById("2")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById("2");

        assertTrue(result.isEmpty());
    }

    @Test
    void updateUser_ShouldUpdateExistingUser() {
        User existingUser = new User("oldUser", "oldPass", "old@example.com", "111", Role.VIEWER);
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedInfo = new User("newUser", "newPass", "new@example.com", "222", Role.ADMIN);

        User updatedUser = userService.updateUser("1", updatedInfo);

        assertEquals("newUser", updatedUser.getUsername());
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("222", updatedUser.getPhone());
        assertEquals(Role.ADMIN, updatedUser.getRole());

        // parola trebuie să fie criptată și diferită de newPass simplu
        assertNotEquals("newPass", updatedUser.getPassword());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("newPass", updatedUser.getPassword()));
    }

    @Test
    void updateUser_ShouldThrowWhenUserNotFound() {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        User updatedInfo = new User("any", "any", "any", "any", Role.VIEWER);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser("99", updatedInfo);
        });

        assertEquals("User not found", exception.getMessage());
    }
}
