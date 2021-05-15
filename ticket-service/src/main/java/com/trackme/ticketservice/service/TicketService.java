package com.trackme.ticketservice.service;

import com.trackme.common.service.ProjectProxyService;
import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.payload.request.ticket.AssignRemoveTicketRequest;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.models.ticket.AssignedEntity;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.utils.TicketUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

        UserEntity user = userService.getUser();
        ProjectEntity project = projectProxyService.getProject(request.getProjectId());
        TicketUtils.checkIfUserAssignToProject(user, project);

        TicketEntity ticket = TicketUtils.buildTicket(request, user, project);

        TicketEntity savedTicket = ticketDbService.saveTicket(ticket);

        return CommonResponse.ok(savedTicket);
    }

    public CommonResponse<TicketEntity> updateTicket(UpdateTicketRequest request) {
        UserEntity user = userService.getUser();
        TicketEntity ticket = ticketDbService.getTicket(request.getTicketId());
        AssignedEntity assignedEntity = TicketUtils.checkIfUserAssignToTicket(user, ticket);

        TicketEntity updatedTicket = TicketUtils.updateTicket(ticket, request, assignedEntity);

        TicketEntity savedTicket = ticketDbService.saveTicket(updatedTicket);

        return CommonResponse.ok(savedTicket);
    }

    public CommonResponse<TicketEntity> deleteTicket(Long id) {
        UserEntity user = userService.getUser();
        TicketEntity ticket = ticketDbService.getTicket(id);
        AssignedEntity assignedEntity = TicketUtils.checkIfUserAssignToTicket(user, ticket);

        ticketDbService.deleteTicket(id);

        return CommonResponse.ok(ticket);
    }

    public CommonResponse<TicketEntity> assignToTicket(AssignRemoveTicketRequest request) {
        // get auth user
        UserEntity user = userService.getUser();
        // get ticket
        TicketEntity ticket = ticketDbService.getTicket(request.getTicketId());
        // check if auth user assigned to ticket
        AssignedEntity assignedEntity = TicketUtils.checkIfUserAssignToTicket(user, ticket);

        // get user details of user to assign
        UserEntity userToAssign = userService.getUserDetails(GetUserDetailsRequest.builder().email(request.getEmail()).build());

        ticket = TicketUtils.assignToTicket(ticket, userToAssign);

        TicketEntity savedTicket = ticketDbService.saveTicket(ticket);

        return CommonResponse.ok(savedTicket);
    }

    public CommonResponse<TicketEntity> removeFromTicket(AssignRemoveTicketRequest request) {
        // get auth user
        UserEntity user = userService.getUser();
        // get ticket
        TicketEntity ticket = ticketDbService.getTicket(request.getTicketId());
        // check if auth user assigned to ticket
        AssignedEntity assignedEntity = TicketUtils.checkIfUserAssignToTicket(user, ticket);

        // get user details of user to assign
        UserEntity userToRemove = userService.getUserDetails(GetUserDetailsRequest.builder().email(request.getEmail()).build());

        ticket = TicketUtils.removeFromTicket(ticket, userToRemove);

        TicketEntity savedTicket = ticketDbService.saveTicket(ticket);

        return CommonResponse.ok(savedTicket);
    }
}
