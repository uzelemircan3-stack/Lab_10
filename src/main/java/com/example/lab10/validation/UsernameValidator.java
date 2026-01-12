package com.example.lab10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        // Kural: Boşluk içeremez ve 'admin' kelimesini barındıramaz [cite: 136]
        return !value.contains(" ") && !value.toLowerCase().contains("admin");
    }
}