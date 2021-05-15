package com.trackme.models.payload.request.ticket;

import com.trackme.models.constants.RegexValidationConstants;
import lombok.*;

import javax.validation.constraints.Email;
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
    @Email(regexp = RegexValidationConstants.EMAIL_REGEX, message = "email format is invalid")
    private String email;
}
