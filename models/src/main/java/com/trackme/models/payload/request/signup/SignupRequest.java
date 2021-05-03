package com.trackme.models.payload.request.signup;

import com.trackme.models.constants.RegexValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 6, max = 40, message = "Username length must be between 6 and 40")
    private String username;

    @Email(regexp = RegexValidationConstants.EMAIL_REGEX, message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = RegexValidationConstants.BCRYPT_PATTERN_REGEX, message = "Password is not encrypted")
    private String password;

    private String role;

    @NotBlank(message = "OrgId must not be blank")
    private String orgId;
}
