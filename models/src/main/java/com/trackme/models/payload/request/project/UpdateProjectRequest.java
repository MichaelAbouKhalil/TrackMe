package com.trackme.models.payload.request.project;

import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.validation.ValidateEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateProjectRequest {

    @NotNull(message = "project id cannot be null")
    private Long projectId;

    private String newName;

    private String description;

    @ValidateEnum(targetClassType = ProjectStatusEnum.class, message = "project status value incorrect")
    private String status;
}
