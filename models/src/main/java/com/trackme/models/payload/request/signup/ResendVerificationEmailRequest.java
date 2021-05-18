package com.trackme.models.payload.request.signup;

import com.trackme.models.constants.RegexValidationConstants;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResendVerificationEmailRequest {

    @Email(regexp = RegexValidationConstants.EMAIL_REGEX, message = "email format invalid")
    @NotNull(message = "email cannot be null")
    private String email;
}
