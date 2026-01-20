package com.example.lab10.repository;

import com.example.lab10.entity.User; // DİKKAT: Burası entity.User olmalı!
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}