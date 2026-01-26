package com.example.lab10.service;

import com.example.lab10.entity.User;
import com.example.lab10.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void saveUser_shouldEncodePasswordAndSave() {
        User user = new User();
        user.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.saveUser(user);

        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(user);
    }

    @Test
    void findByUsername_whenUserExists_shouldReturnUser() {
        String username = "marvin";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void findByUsername_whenUserDoesNotExist_shouldReturnNull() {
        when(userRepository.findByUsername("none")).thenReturn(Optional.empty());

        User result = userService.findByUsername("none");

        assertNull(result);
    }
}