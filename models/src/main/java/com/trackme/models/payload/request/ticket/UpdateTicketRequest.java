package com.trackme.models.payload.request.ticket;

import com.trackme.models.enums.CriticalLevelEnum;
import com.trackme.models.enums.TicketStatusEnum;
import com.trackme.models.validation.ValidateEnum;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateTicketRequest {

    @NotNull(message = "ticket id cannot be null")
    private Long ticketId;

    private String title;

    @ValidateEnum(targetClassType = CriticalLevelEnum.class)
    private String criticalLevel;

    private String description;

    private String reproductionSteps;

    @ValidateEnum(targetClassType = TicketStatusEnum.class)
    private String status;
}
