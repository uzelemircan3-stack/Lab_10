package com.example.lab10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface ValidUsername {
    String message() default "Username cannot contain spaces or the word 'admin'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}