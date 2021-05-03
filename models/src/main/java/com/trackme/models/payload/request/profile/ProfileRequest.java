package com.trackme.models.payload.request.profile;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProfileRequest {

    @NotBlank(message = "FirstName cannot be empty or null.")
    private String firstName;

    @NotBlank(message = "LastName cannot be empty or null.")
    private String lastName;
}
