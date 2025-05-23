package com.example.demo.controller;

import com.example.demo.Controller.UserController;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;


@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnCreated() throws Exception {
        // Arrange
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstName("Tudor");
        requestDto.setLastName("Doe");
        requestDto.setUserName("johndoe");
        requestDto.setEmail("john@example.com");

        // Create response DTO that matches the request
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(UUID.randomUUID());
        responseDto.setFirstName(requestDto.getFirstName());  // Use the same values as request
        responseDto.setLastName(requestDto.getLastName());
        responseDto.setUserName(requestDto.getUserName());
        responseDto.setEmail(requestDto.getEmail());

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Tudor"))  // Check actual input values
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.userName").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        // Verify the service was called with correct data
        ArgumentCaptor<UserRequestDto> userCaptor = ArgumentCaptor.forClass(UserRequestDto.class);
        verify(userService).createUser(userCaptor.capture());
        UserRequestDto capturedRequest = userCaptor.getValue();
        assertEquals("Tudor", capturedRequest.getFirstName());
        assertEquals("Doe", capturedRequest.getLastName());
    }


   @Test
    void getUserById_ShouldReturnUser() throws Exception {
       UUID id = UUID.randomUUID();
      when(userService.getUserById(id)).thenReturn(new UserResponseDto());
       ResultActions resultActions = mockMvc.perform(get("/api/users/{id}", id))
               .andExpect(status().isOk());
   }
}
