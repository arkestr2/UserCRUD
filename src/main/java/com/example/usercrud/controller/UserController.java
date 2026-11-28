package com.example.usercrud.controller;

import com.example.usercrud.dto.UserListResponseDto;
import com.example.usercrud.dto.UserRequestDto;
import com.example.usercrud.dto.UserResponseDto;
import com.example.usercrud.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> save(
            @RequestBody @Valid UserRequestDto userRequestDto
    ) {
        UserResponseDto saved = userService.save(userRequestDto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> findById(
            @PathVariable Long id
    ) {
        UserResponseDto found = userService.findById(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping("/users")
    public ResponseEntity<UserListResponseDto> findAll() {
        UserListResponseDto found = userService.findAll();
        return ResponseEntity.ok(found);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestDto userRequestDto
    ) {
        UserResponseDto updated = userService.update(id, userRequestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id
    ) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
