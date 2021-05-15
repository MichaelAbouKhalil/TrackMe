package com.trackme.models.common;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtToken {

    @JsonProperty("user_name")
    private String username;

    private Long userId;
    private String email;
    private String orgId;
    private String[] authorities;
}
