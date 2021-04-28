package com.trackme.models.payload.request.roleupdate;

import com.trackme.common.constant.RegexValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleUpdateRequest {

    @Email(regexp = RegexValidationConstants.EMAIL_REGEX, message = "Email is not valid")
    private String email;
}
