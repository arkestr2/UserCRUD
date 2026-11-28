package com.example.usercrud.dto;

import jakarta.validation.constraints.*;

public record UserRequestDto(
        String name,

        @Email
        @Size(min = 1, max = 255)
        String email,

        @Min(18)
        @Max(150)
        Integer age
) {
}
