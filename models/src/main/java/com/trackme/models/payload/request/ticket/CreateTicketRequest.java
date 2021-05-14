package com.trackme.models.payload.request.ticket;

import com.trackme.models.enums.CriticalLevelEnum;
import com.trackme.models.validation.ValidateEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateTicketRequest {

    @NotNull(message = "project id cannot be null")
    private Long projectId;

    @NotBlank(message = "title cannot be blank")
    private String title;

    @ValidateEnum(targetClassType = CriticalLevelEnum.class,
    message = "critical level cannot be blank")
    private String criticalLevel;

    private String description;

    private String reproductionSteps;
}
