package com.example.usercrud.dto;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        int age,
        Instant createdAt
) {
}
