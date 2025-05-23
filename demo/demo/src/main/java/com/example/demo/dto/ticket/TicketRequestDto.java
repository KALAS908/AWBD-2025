package com.example.demo.dto.ticket;

import com.example.demo.Models.Enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDto {
    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @NotNull(message = "Subject cannot be null")
    private String subject;

    @NotNull(message = "Status cannot be null")
    private TicketStatus status;
}
