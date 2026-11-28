package com.example.usercrud.dto;

import jakarta.validation.constraints.*;

public record UserRequestDto(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @Min(18)
        @Max(150)
        @NotNull
        Integer age
) {
}
