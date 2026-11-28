package com.example.usercrud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(
        @NotNull
        String name,

        @Email
        @NotNull
        String email,

        @Min(18)
        @Max(150)
        @NotNull
        Integer age
) {
}
