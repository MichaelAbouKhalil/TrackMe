package com.trackme.models.payload.request.ticket;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AssignRemoveTicketRequest {

    @NotNull(message = "ticket id cannot be null")
    private Long ticketId;

    @NotBlank(message = "email cannot be blank")
    private String email;
}
