package com.example.usercrud.util;

import com.example.usercrud.dto.UserRequestDto;
import com.example.usercrud.dto.UserResponseDto;
import com.example.usercrud.model.User;

public class MappingUtil {

    public static UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public static User toModel(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.name(),
                userRequestDto.email(),
                userRequestDto.age()
        );
    }
}
