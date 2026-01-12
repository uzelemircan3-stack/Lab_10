package com.example.lab10.dto;

import com.example.lab10.validation.ValidUsername; // Custom validator paketiniz
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username bos olamaz")
    @Size(min = 3, message = "Username en az 3 karakter olmali")
    @ValidUsername // Task 3: Custom Validator kullanımı [cite: 15, 30]
    private String username;

    @Email(message = "Gecerli bir email giriniz") // [cite: 14, 28]
    @NotBlank(message = "Email zorunludur")
    private String email;

    @NotBlank(message = "Sifre bos olamaz")
    @Size(min = 6, message = "Sifre en az 6 karakter olmali")
    private String password;
}