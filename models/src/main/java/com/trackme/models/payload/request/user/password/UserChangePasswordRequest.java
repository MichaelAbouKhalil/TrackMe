package com.trackme.models.payload.request.user.password;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserChangePasswordRequest {

    @NotBlank(message = "old password cannot be blank")
    private String oldPassword;

    @NotBlank(message = "new password cannot be blank")
    private String newPassword;
}
