package com.trackme.models.payload.request.project;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NewProjectRequest {

    @NotBlank(message = "project name cannot be blank")
    private String name;

    private String description;
}
