package com.example.lab10.controller;

import com.example.lab10.dto.CreateUserRequest;
import com.example.lab10.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Task 1: API Route (REST) [cite: 7]
    @GetMapping("/register")
    public ResponseEntity<String> getRegisterPage() {
        return ResponseEntity.ok("Lab 10: Lutfen kayit icin POST istegi gonderin.");
    }

    // Task 2: Manuel Request Header okuma (@RequestHeader) [cite: 11, 24]
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody CreateUserRequest request,
            @RequestHeader(value = "User-Agent", defaultValue = "Unknown") String userAgent) {

        System.out.println("Gelen Header: " + userAgent); //

        try {
            userService.createUser(request);
            // Task 4: Doğru status code (201 Created) [cite: 17, 32]
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            // Veritabanı hatasını kullanıcıya anlamlı dön [cite: 34]
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Veritabani hatasi: " + e.getMessage());
        }
    }
}