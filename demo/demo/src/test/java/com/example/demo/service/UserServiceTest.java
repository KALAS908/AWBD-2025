package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.Models.User;
import com.example.demo.dto.user.CredentialsDto;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // Test data
    private final UUID userId = UUID.randomUUID();
    private final UserRequestDto userRequestDto = new UserRequestDto(
            "John", "Doe", "johndoe", 30, "john@example.com", "password"
    );
    private final User userEntity = User.builder()
            .id(userId)
            .firstName("John")
            .lastName("Doe")
            .userName("johndoe")
            .age(30)
            .email("john@example.com")
            .password("password")
            .build();

    @Test
    void createUser_Success() {
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(null);
        when(userRepository.findByUserName(userRequestDto.getUserName())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserResponseDto response = userService.createUser(userRequestDto);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_EmailExists() {
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(userEntity);

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.createUser(userRequestDto)
        );

        assertEquals("Email already exists: " + userRequestDto.getEmail(), exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserResponseDto response = userService.getUserById(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserById(userId)
        );

        assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test
    void updateUser_Success() {
        UserRequestDto updateDto = new UserRequestDto(
                "Updated", "Name", "updated", 35, "updated@example.com", "newpassword"
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(User.class))).thenReturn(userEntity);

        UserResponseDto response = userService.updateUser(userId, updateDto);

        assertNotNull(response);
        verify(userRepository).save(userEntity);
        assertEquals("Updated", userEntity.getFirstName());
        assertEquals("Name", userEntity.getLastName());
        assertEquals("updated", userEntity.getUserName());
        assertEquals(35, userEntity.getAge());
        assertEquals("updated@example.com", userEntity.getEmail());
        assertEquals("newpassword", userEntity.getPassword());
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).delete(userEntity);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(userEntity);
    }

    @Test
    void getAllUsers_Success() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(userEntity, users.get(0));
    }

    @Test
    void login_Success() {
        CredentialsDto credentials = new CredentialsDto("john@example.com", "password");
        when(userRepository.findByEmail(credentials.getEmail())).thenReturn(userEntity);

        UserResponseDto response = userService.login(credentials);

        assertNotNull(response);
        assertEquals(userId, response.getId());
    }

    @Test
    void login_InvalidEmail() {
        CredentialsDto credentials = new CredentialsDto("wrong@example.com", "password");
        when(userRepository.findByEmail(credentials.getEmail())).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.login(credentials)
        );

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_InvalidPassword() {
        CredentialsDto credentials = new CredentialsDto("john@example.com", "wrongpass");
        when(userRepository.findByEmail(credentials.getEmail())).thenReturn(userEntity);

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.login(credentials)
        );

        assertEquals("Invalid email or password", exception.getMessage());
    }
}
