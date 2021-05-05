package com.trackme.models.payload.request.project;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DeleteProjectRequest {

    @NotNull(message = "project id cannot be null")
    private Long projectId;
}
