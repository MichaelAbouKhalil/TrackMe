package com.trackme.ticketservice.utils;

import com.trackme.models.enums.RoleEnum;
import com.trackme.models.enums.TicketStatusEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.models.ticket.AssignedEntity;
import com.trackme.models.ticket.TicketEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
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

    public static AssignedEntity checkIfUserAssignToTicket(UserEntity user, TicketEntity ticket) {
        log.info("checking if user [{}] is assigned to ticket [{}]", user.getUsername(), ticket.getTitle());

        Set<AssignedEntity> assignedPersonnel = ticket.getAssignedPersonnel();
        AssignedEntity assignedEntity = null;
        for (AssignedEntity assigned : assignedPersonnel) {
            if (assigned.getEmail().equals(user.getEmail())) {
                assignedEntity = assigned;
            }
        }

        if (assignedEntity == null) {
            throw new InvalidOperationException("user [" + user.getUsername() + "] is not assigned to the ticket");
        }

        return assignedEntity;
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

    public static TicketEntity updateTicket(TicketEntity ticket, UpdateTicketRequest request, AssignedEntity assigned) {

        if (!StringUtils.isEmpty(request.getTitle())) {
            ticket.setTitle(request.getTitle());
        }
        if (!StringUtils.isEmpty(request.getCriticalLevel())) {
            ticket.setCriticalLevel(request.getCriticalLevel());
        }
        if (!StringUtils.isEmpty(request.getDescription())) {
            ticket.setDescription(request.getDescription());
        }
        if (!StringUtils.isEmpty(request.getReproductionSteps())) {
            ticket.setReproductionSteps(request.getReproductionSteps());
        }
        if (!StringUtils.isEmpty(request.getStatus())) {
            checkSuccessiveStatus(ticket.getStatus(), request.getStatus());

            if (TicketStatusEnum.isEndStatus(request.getStatus())) {
                assigned.setClosed(ticket);
                ticket.setClosedDate(LocalDateTime.now());
                ticket.setClosedBy(assigned);
            }
            ticket.setStatus(request.getStatus());
        }
        return ticket;
    }

    private static void checkSuccessiveStatus(String ticketStatus, String nextStatus) {
        TicketStatusEnum ticketStatusEnum = TicketStatusEnum.getStatusByName(ticketStatus);
        TicketStatusEnum nextStatusEnum = TicketStatusEnum.getStatusByName(nextStatus);

        if (!ticketStatusEnum.isNextStatusAccepted(nextStatusEnum)) {
            throw new InvalidOperationException("status to be updated in invalid, " +
                    "please send valid status for status [" + ticketStatus + "]");
        }

    }

    public static TicketEntity assignToTicket(TicketEntity ticket, UserEntity userToAssign) {
        log.info("assigning user [{}] to ticket with id [{}]", userToAssign.getEmail(), ticket.getId());

        Set<AssignedEntity> assignedPersonnel = ticket.getAssignedPersonnel();
        if (contains(assignedPersonnel, userToAssign.getEmail())) {
            log.info("user [{}] already assigned to ticket [{}]", userToAssign.getEmail(), ticket.getId());
            return ticket;
        }

        Set<AssignedEntity> set = assignedPersonnel;
        set.add(AssignedEntity.builder().email(userToAssign.getEmail())
                .role(userToAssign.getRoles().get(0).getRoleName()).ticket(ticket).build());
        return ticket;
    }

    public static TicketEntity removeFromTicket(TicketEntity ticket, UserEntity userToRemove) {
        log.info("removing user [{}] from ticket with id [{}]", userToRemove.getEmail(), ticket.getId());

        Set<AssignedEntity> assignedPersonnel = ticket.getAssignedPersonnel();
        if (!contains(assignedPersonnel, userToRemove.getEmail())) {
            log.info("user [{}] is not assigned to ticket [{}]", userToRemove.getEmail(), ticket.getId());
            return ticket;
        }

        Set<AssignedEntity> set = ticket.getAssignedPersonnel();
        Iterator<AssignedEntity> iterator = set.iterator();
        while (iterator.hasNext()) {
            AssignedEntity temp = iterator.next();
            if (temp.getEmail().equals(userToRemove.getEmail())) {
                iterator.remove();
            }
        }
        return ticket;
    }

    private static boolean contains(Set<AssignedEntity> set, String email) {
        for (AssignedEntity a : set) {
            if (a.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
