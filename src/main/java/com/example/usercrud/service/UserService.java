package com.example.usercrud.service;

import com.example.usercrud.dto.UserListResponseDto;
import com.example.usercrud.dto.UserRequestDto;
import com.example.usercrud.dto.UserResponseDto;
import com.example.usercrud.exception.UserNotFoundException;
import com.example.usercrud.model.User;
import com.example.usercrud.repository.UserRepository;
import com.example.usercrud.util.MappingUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto save(UserRequestDto userRequestDto) {
        log.info("Creating user: {}", userRequestDto);

        User user = MappingUtil.toModel(userRequestDto);
        UserResponseDto savedDto = MappingUtil.toDto(userRepository.save(user));

        log.info("Saved user with ID: {}", savedDto.id());

        return savedDto;
    }

    public UserResponseDto findById(Long id) {
        log.info("Searching user by ID: {}", id);

        User found = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        log.info("User found by ID: {}", id);

        return MappingUtil.toDto(found);
    }

    public UserListResponseDto findAll() {
        log.info("Searching all users");

        return new UserListResponseDto(
                userRepository.findAll().stream().map(MappingUtil::toDto).toList()
        );
    }

    @Transactional
    public UserResponseDto update(Long id, UserRequestDto userUpdate) {
        log.info("Updating user with ID: {}", id);

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userToUpdate.setName(userUpdate.name());
        userToUpdate.setEmail(userUpdate.email());
        userToUpdate.setAge(userUpdate.age());

        User updatedUser = userRepository.save(userToUpdate);
        log.info("Updated user with ID: {}", id);

        return MappingUtil.toDto(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting user with ID: {}", id);

        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.delete(userToDelete);
        log.info("Deleted user with ID: {}", id);
    }
}
