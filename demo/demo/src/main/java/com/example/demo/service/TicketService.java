package com.example.demo.service;

import com.example.demo.Models.Enums.TicketStatus;
import com.example.demo.Models.Ticket;
import com.example.demo.Models.User;
import com.example.demo.dto.ticket.TicketRequestDto;
import com.example.demo.dto.ticket.TicketResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    // Create
    public TicketResponseDto createTicket(TicketRequestDto ticketRequestDto) {
        User user = userRepository.findById(ticketRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setSubject(ticketRequestDto.getSubject());
        ticket.setStatus(ticketRequestDto.getStatus());

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToDto(savedTicket);
    }

    // Read
    public TicketResponseDto getTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return mapToDto(ticket);
    }

    public List<TicketResponseDto> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<TicketResponseDto> getTicketsByUser(UUID userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update
    public TicketResponseDto updateTicket(UUID id, TicketRequestDto ticketRequestDto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if (ticketRequestDto.getSubject() != null) {
            ticket.setSubject(ticketRequestDto.getSubject());
        }
        if (ticketRequestDto.getStatus() != null) {
            ticket.setStatus(ticketRequestDto.getStatus());
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return mapToDto(updatedTicket);
    }

    public TicketResponseDto updateTicketStatus(UUID id, TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));

        ticket.setStatus(status);
        Ticket updatedTicket = ticketRepository.save(ticket);

        return mapToDto(updatedTicket);
    }

    // Delete
    public void deleteTicket(UUID id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    // Helper method
    private TicketResponseDto mapToDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getUser().getId(),
                ticket.getSubject(),
                ticket.getStatus()
        );
    }
}

