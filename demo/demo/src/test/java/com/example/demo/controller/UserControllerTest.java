package com.example.demo.controller;

import com.example.demo.Controller.UserController;
import com.example.demo.Models.User;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();

    @Test
    void getUserById_ReturnsUser() throws Exception {
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .userName("johndoe")
                .age(30)
                .email("john@example.com")
                .token("sometoken")
                .message("User found")
                .isAdmin(false)
                .build();

        Mockito.when(userService.getUserById(userId)).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.userName").value("johndoe"))
                .andExpect(jsonPath("$.token").value("sometoken"))
                .andExpect(jsonPath("$.message").value("User found"))
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    void updateUser_ReturnsUpdatedUser() throws Exception {
        UserRequestDto updateDto = new UserRequestDto(
                "Jane", "Smith", "janesmith", 28, "jane@example.com", "password"
        );
        UserResponseDto updatedResponse = UserResponseDto.builder()
                .id(userId)
                .firstName("Jane")
                .lastName("Smith")
                .userName("janesmith")
                .age(28)
                .email("jane@example.com")
                .token(null)
                .message("User updated")
                .isAdmin(false)
                .build();

        Mockito.when(userService.updateUser(eq(userId), any(UserRequestDto.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.userName").value("janesmith"))
                .andExpect(jsonPath("$.message").value("User updated"));
    }

    @Test
    void deleteUser_ReturnsNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() throws Exception {
        User user = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Doe")
                .userName("johndoe")
                .age(30)
                .email("john@example.com")
                .password("password")
                .build();
        List<User> users = Collections.singletonList(user);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].userName").value("johndoe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }
}
