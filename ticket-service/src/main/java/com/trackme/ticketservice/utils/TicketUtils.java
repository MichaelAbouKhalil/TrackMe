package com.trackme.ticketservice.utils;

import com.trackme.models.enums.RoleEnum;
import com.trackme.models.enums.TicketStatusEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.models.ticket.AssignedEntity;
import com.trackme.models.ticket.TicketEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class TicketUtils {

    public static void checkIfUserAssignToProject(UserEntity user, ProjectEntity project) {
        log.info("checking if user [{}] is assigned to project [{}]", user.getUsername(), project.getName());

        RoleEntity userRole = user.getRoles().get(0);
        RoleEnum role = RoleEnum.returnByName(userRole.getRoleName());

        Set<String> assigned = new HashSet<>();
        if (RoleEnum.PM.equals(role)) {
            assigned = project.getAssignedPms();
        } else if (RoleEnum.DEV.equals(role)) {
            assigned = project.getAssignedDevs();
        } else if (RoleEnum.CUSTOMER.equals(role)) {
            assigned = project.getAssignedCustomers();
        }

        if (assigned.isEmpty() || !assigned.contains(user.getEmail())) {
            throw new InvalidOperationException("user [" + user.getUsername() + "] is not assigned to the project");
        }
    }

    public static TicketEntity buildTicket(CreateTicketRequest request, UserEntity user, ProjectEntity project) {
        AssignedEntity assigned = AssignedEntity.builder()
                .email(user.getEmail())
                .role(user.getRoles().get(0).getRoleName())
                .build();
        TicketEntity ticket = TicketEntity.builder()
                .projectId(project.getId())
                .title(request.getTitle())
                .status(TicketStatusEnum.NEW_TICKET.getName())
                .criticalLevel(request.getCriticalLevel())
                .description(request.getDescription())
                .reproductionSteps(request.getReproductionSteps())
                .build();

        Set<AssignedEntity> set = new HashSet<>(ticket.getAssignedPersonnel());
        set.add(assigned);
        ticket.setAssignedPersonnel(set);
        ticket.setOpenedBy(assigned);

        assigned.setTicket(ticket);
        assigned.setOpened(ticket);
        return ticket;
    }
}
