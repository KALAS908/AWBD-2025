package com.example.demo.dto.ticket;

import com.example.demo.Models.Enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {
    private UUID id;
    private UUID userId;
    private String subject;
    private TicketStatus status;
}
