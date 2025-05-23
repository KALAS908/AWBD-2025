package com.example.demo.controller;

import com.example.demo.Controller.TicketController;
import com.example.demo.Controller.UserController;
import com.example.demo.Models.Enums.TicketStatus;
import com.example.demo.dto.ticket.TicketRequestDto;
import com.example.demo.dto.ticket.TicketResponseDto;
import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.TicketService;
import com.example.demo.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.UUID;


@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    private TicketRequestDto requestDto;
    private TicketResponseDto responseDto;
    private UUID ticketId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        ticketId = UUID.randomUUID();
        userId = UUID.randomUUID();

        requestDto = new TicketRequestDto(
                userId,
                "Technical Issue",
                TicketStatus.OPENED
        );

        responseDto = new TicketResponseDto(
                ticketId,
                userId,
                "Technical Issue",
                TicketStatus.OPENED
        );
    }

    @Test
    void createTicket_ShouldReturnCreatedStatus() throws Exception {
        when(ticketService.createTicket(any(TicketRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subject").value("Technical Issue"))
                .andExpect(jsonPath("$.status").value("OPENED"));
    }

    @Test
    void createTicket_WithNullRequiredFields_ShouldReturnBadRequest() throws Exception {
        TicketRequestDto invalidDto = new TicketRequestDto(
                null,
                null,
                null
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTicket_ShouldReturnOkStatus() throws Exception {
        when(ticketService.updateTicket(any(UUID.class), any(TicketRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tickets/" + ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Technical Issue"));

        verify(ticketService).updateTicket(eq(ticketId), any(TicketRequestDto.class));
    }

    @Test
    void getTicketById_ShouldReturnOkStatus() throws Exception {
        when(ticketService.getTicketById(ticketId))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/tickets/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject").value("Technical Issue"))
                .andExpect(jsonPath("$.status").value("OPENED"));

        verify(ticketService).getTicketById(ticketId);
    }

    @Test
    void getAllTickets_ShouldReturnOkStatus() throws Exception {
        when(ticketService.getAllTickets())
                .thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subject").value("Technical Issue"))
                .andExpect(jsonPath("$[0].status").value("OPENED"));

        verify(ticketService).getAllTickets();
    }

    @Test
    void deleteTicket_ShouldReturnNoContentStatus() throws Exception {
        doNothing().when(ticketService).deleteTicket(ticketId);

        mockMvc.perform(delete("/api/tickets/" + ticketId))
                .andExpect(status().isNoContent());

        verify(ticketService).deleteTicket(ticketId);
    }

    @Test
    void getTicketsByUser_ShouldReturnOkStatus() throws Exception {
        when(ticketService.getTicketsByUser(userId))
                .thenReturn(Arrays.asList(responseDto));

        mockMvc.perform(get("/api/tickets/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].subject").value("Technical Issue"))
                .andExpect(jsonPath("$[0].status").value("OPENED"));

        verify(ticketService).getTicketsByUser(userId);
    }

    @Test
    void updateTicketStatus_ShouldReturnOkStatus() throws Exception {
        // Arrange
        TicketStatus newStatus = TicketStatus.CLOSED;
        responseDto.setStatus(newStatus);
        when(ticketService.updateTicketStatus(ticketId, newStatus))
                .thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"CLOSED\""))  // String value needs to be properly quoted
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));

        verify(ticketService).updateTicketStatus(ticketId, newStatus);
    }

}

