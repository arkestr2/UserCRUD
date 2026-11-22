package com.example.usercrud.service;

import com.example.usercrud.dao.Dao;
import com.example.usercrud.model.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private Dao<User> dao;

    @Test
    void save() {
        User mockUser = mock(User.class);

        userService.save(mockUser);

        verify(dao, times(1)).save(mockUser);
    }

    @Test
    void findById() {
        Long id = 1L;
        User mockUser = mock(User.class);

        when(dao.findById(id)).thenReturn(Optional.of(mockUser));

        Optional<User> user = userService.findById(id);

        assertTrue(user.isPresent());
        assertEquals(mockUser, user.get());
    }

    @Test
    void findAll() {
        User mockUser = mock(User.class);
        when(dao.findAll()).thenReturn(List.of(mockUser));

        List<User> users = userService.findAll();

        assertFalse(users.isEmpty());
        assertEquals(mockUser, users.getFirst());
    }

    @Test
    void update() {
        Long id = 1L;
        String updatedName = "updated_name";

        User mockUser = mock(User.class);

        User mockUserUpdates = mock(User.class);
        when(mockUserUpdates.getName()).thenReturn(updatedName);

        User mockUpdatedUser = mock(User.class);
        when(mockUpdatedUser.getName()).thenReturn(updatedName);

        when(dao.findById(id)).thenReturn(Optional.of(mockUser));
        when(dao.update(mockUser)).thenReturn(mockUpdatedUser);

        Optional<User> user = userService.update(id, mockUserUpdates);

        verify(dao, times(1)).findById(id);
        verify(dao, times(1)).update(mockUser);
        assertTrue(user.isPresent());
        assertEquals(mockUpdatedUser, user.get());
        assertEquals(mockUpdatedUser.getName(), user.get().getName());
    }

    @Test
    void updateNonexistent() {
        when(dao.findById(1L)).thenReturn(Optional.empty());

        assertTrue(userService.update(1L, null).isEmpty());

        verify(dao, never()).update(any());
    }

    @Test
    void delete() {
        Long id = 1L;

        User mockUser = mock(User.class);

        when(dao.findById(id)).thenReturn(Optional.of(mockUser));

        userService.delete(id);

        verify(dao, times(1)).delete(mockUser);
    }

    @Test
    void deleteNonexistent() {
        when(dao.findById(1L)).thenReturn(Optional.empty());

        assertTrue(userService.delete(1L).isEmpty());
        verify(dao, never()).delete(any());
    }
}
