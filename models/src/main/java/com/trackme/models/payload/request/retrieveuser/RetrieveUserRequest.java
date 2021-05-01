package com.trackme.models.payload.request.retrieveuser;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RetrieveUserRequest {

    @NotNull(message = "Username cannot be null")
    private String username;
}
