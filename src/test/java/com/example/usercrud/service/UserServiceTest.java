package com.example.usercrud.service;

import com.example.usercrud.dto.UserListResponseDto;
import com.example.usercrud.dto.UserRequestDto;
import com.example.usercrud.dto.UserResponseDto;
import com.example.usercrud.exception.UserNotFoundException;
import com.example.usercrud.model.User;

import com.example.usercrud.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void save() {
        UserRequestDto userRequestDto = new UserRequestDto(
                "test",
                "test@email.com",
                30
        );

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getName()).thenReturn("test");
        when(mockUser.getEmail()).thenReturn("test@email.com");
        when(mockUser.getAge()).thenReturn(30);
        when(mockUser.getCreatedAt()).thenReturn(mock(Instant.class));

        when(userRepository.save(any())).thenReturn(mockUser);
        userService.save(userRequestDto);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findById() {
        Long id = 1L;
        User mockUser = mock(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        UserResponseDto found = userService.findById(id);

        assertEquals(mockUser.getId(), found.id());
    }

    @Test
    void findAll() {
        User mockUser = mock(User.class);
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        UserListResponseDto users = userService.findAll();

        assertFalse(users.data().isEmpty());
        assertEquals(mockUser.getId(), users.data().getFirst().id());
    }

    @Test
    void update() {
        Long id = 1L;

        UserRequestDto userUpdate = new UserRequestDto(
                "updated_name",
                "updated@mail.com",
                30
        );

        User mockUser = mock(User.class);
        User mockUpdatedUser = mock(User.class);
        when(mockUpdatedUser.getId()).thenReturn(1L);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUpdatedUser);

        UserResponseDto userResponseDto = userService.update(id, userUpdate);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(mockUser);

        verify(mockUser).setName("updated_name");
        verify(mockUser).setEmail("updated@mail.com");
        verify(mockUser).setAge(30);

        assertEquals(mockUpdatedUser.getId(), userResponseDto.id());
    }

    @Test
    void updateNonexistent() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(1L, null));

        verify(userRepository, never()).save(any());
    }

    @Test
    void delete() {
        Long id = 1L;

        User mockUser = mock(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));

        userService.delete(id);

        verify(userRepository, times(1)).delete(mockUser);
    }

    @Test
    void deleteNonexistent() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,  () -> userService.delete(1L));
        verify(userRepository, never()).delete(any());
    }
}
