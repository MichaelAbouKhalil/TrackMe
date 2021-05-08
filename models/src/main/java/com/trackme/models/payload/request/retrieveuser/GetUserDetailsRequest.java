package com.trackme.models.payload.request.retrieveuser;

import com.trackme.models.constants.RegexValidationConstants;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GetUserDetailsRequest {

    private String username;

    @Email(regexp = RegexValidationConstants.EMAIL_REGEX,
            message = "email is invalid")
    private String email;
}
