package com.example.demo.service;

import com.example.demo.Models.User;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldReturnSavedUser() {
        User user = new User();
        when(userRepository.save(any())).thenReturn(user);

        UserRequestDto requestDto = new UserRequestDto();
        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        verify(userRepository).save(any());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserById(id);

        assertNotNull(result);
        verify(userRepository).findById(id);
    }
}
