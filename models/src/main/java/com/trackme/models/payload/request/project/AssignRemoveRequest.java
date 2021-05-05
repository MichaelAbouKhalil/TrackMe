package com.trackme.models.payload.request.project;

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
public class AssignRemoveRequest {

    @NotNull(message = "project id cannot be null")
    private Long projectId;

    @Email(regexp = RegexValidationConstants.EMAIL_REGEX, message = "email format is incorrect")
    private String email;

}
