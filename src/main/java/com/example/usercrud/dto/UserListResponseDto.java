package com.example.usercrud.dto;

import java.util.List;

public record UserListResponseDto(
        List<UserResponseDto> data
) {
}
