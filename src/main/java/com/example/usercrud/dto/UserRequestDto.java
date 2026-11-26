package com.example.usercrud.dto;

public record UserRequestDto(
        String name,
        String email,
        Integer age
) {
}
