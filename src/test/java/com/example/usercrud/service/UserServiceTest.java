package com.example.usercrud.service;

import com.example.usercrud.dto.UserRequestDto;
import com.example.usercrud.dto.UserResponseDto;
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
        UserRequestDto mockUserRequestDto = mock(UserRequestDto.class);
        when(mockUserRequestDto.name()).thenReturn("test");
        when(mockUserRequestDto.email()).thenReturn("test@email.com");
        when(mockUserRequestDto.age()).thenReturn(30);

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getName()).thenReturn("test");
        when(mockUser.getEmail()).thenReturn("test@email.com");
        when(mockUser.getAge()).thenReturn(30);
        when(mockUser.getCreatedAt()).thenReturn(mock(Instant.class));

        when(userRepository.save(any())).thenReturn(mockUser);
        userService.save(mockUserRequestDto);

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

        List<UserResponseDto> users = userService.findAll();

        assertFalse(users.isEmpty());
        assertEquals(mockUser.getId(), users.getFirst().id());
    }

    @Test
    void update() {
        Long id = 1L;
        String updatedName = "updated_name";

        User mockUser = mock(User.class);

        UserRequestDto mockUserUpdates = mock(UserRequestDto.class);
        when(mockUserUpdates.name()).thenReturn(updatedName);

        User mockUpdatedUser = mock(User.class);
        when(mockUpdatedUser.getName()).thenReturn(updatedName);

        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUpdatedUser);

        UserResponseDto userResponseDto = userService.update(id, mockUserUpdates);

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(mockUser);

        verify(mockUser).setName("updated_name");
        verify(mockUser, never()).setEmail(any());
        verify(mockUser, never()).setAge(anyInt());

        assertEquals(mockUpdatedUser.getId(), userResponseDto.id());
    }

    @Test
    void updateNonexistent() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.update(1L, null));

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

        assertThrows(IllegalArgumentException.class,  () -> userService.delete(1L));
        verify(userRepository, never()).delete(any());
    }
}
