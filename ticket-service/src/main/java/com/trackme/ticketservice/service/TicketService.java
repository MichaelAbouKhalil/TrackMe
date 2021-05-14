package com.trackme.ticketservice.service;

import com.trackme.common.service.ProjectProxyService;
import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.ticket.AssignRemoveTicketRequest;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.utils.TicketUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketDbService ticketDbService;
    private final UserService userService;
    private final ProjectProxyService projectProxyService;

    public CommonResponse<TicketEntity> getTicket(Long id) {

        UserEntity user = userService.getUser();

        TicketEntity ticket = ticketDbService.getTicket(id);

        // check if user assigned to project for this ticket
        ProjectEntity project = projectProxyService.getProject(ticket.getProjectId());

        TicketUtils.checkIfUserAssignToProject(user, project);

        return CommonResponse.ok(ticket);
    }

    public CommonResponse<TicketEntity> createTicket(CreateTicketRequest request) {
        return null;
    }

    public CommonResponse<TicketEntity> updateTicket(UpdateTicketRequest request) {
        return null;
    }

    public CommonResponse<TicketEntity> deleteTicket(Long id) {
        return null;
    }

    public CommonResponse<TicketEntity> assignToTicket(AssignRemoveTicketRequest request) {
        return null;
    }

    public CommonResponse<TicketEntity> removeFromTicket(AssignRemoveTicketRequest request) {
        return null;
    }
}
