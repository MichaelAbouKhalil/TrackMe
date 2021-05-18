package com.trackme.ticketservice.service;

import com.trackme.common.service.ProjectProxyService;
import com.trackme.common.service.UserService;
import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.CriticalLevelEnum;
import com.trackme.models.enums.ProjectStatusEnum;
import com.trackme.models.enums.RoleEnum;
import com.trackme.models.enums.TicketStatusEnum;
import com.trackme.models.exception.InvalidOperationException;
import com.trackme.models.exception.NotFoundException;
import com.trackme.models.payload.request.retrieveuser.GetUserDetailsRequest;
import com.trackme.models.payload.request.ticket.AssignRemoveTicketRequest;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.project.ProjectEntity;
import com.trackme.models.security.RoleEntity;
import com.trackme.models.security.UserEntity;
import com.trackme.models.ticket.AssignedEntity;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.Base;
import com.trackme.ticketservice.utils.TicketUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TicketServiceTest extends Base {

    @Autowired
    TicketService ticketService;

    @MockBean
    TicketDbService ticketDbService;

    @MockBean
    UserService userService;

    @MockBean
    ProjectProxyService projectProxyService;

    TicketEntity ticket;
    TicketEntity ticket2;
    UserEntity user;
    ProjectEntity project;
    UserEntity userToAssignRemove;

    CreateTicketRequest createTicketRequest;
    UpdateTicketRequest updateTicketRequest;
    AssignRemoveTicketRequest assignRemoveTicketRequest;

    @BeforeEach
    void setUp() {
        Mockito.reset(ticketDbService, userService, projectProxyService);
        user = UserEntity.builder().email("pm@test.com").id(1L)
                .username("pm")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.PM.getRoleName()).build())
                .build();
        project = ProjectEntity.builder().id(1L).name("test-proj")
                .status(ProjectStatusEnum.OPEN_PROJECT.getName())
                .assignedPm(user.getEmail())
                .build();
        ticket = TicketEntity.builder().projectId(project.getId())
                .title("test-ticket").criticalLevel(CriticalLevelEnum.HIGH.getName())
                .status(TicketStatusEnum.NEW_TICKET.getName())
                .id(1L).assign(AssignedEntity.builder()
                        .email(user.getEmail())
                        .role(user.getRoles().get(0).getRoleName()).build())
                .build();
        ticket2 = TicketEntity.builder().projectId(project.getId())
                .title("test-ticket2").criticalLevel(CriticalLevelEnum.LOW.getName())
                .status(TicketStatusEnum.IN_PROGRESS.getName())
                .id(1L).assign(AssignedEntity.builder()
                        .email(user.getEmail())
                        .role(user.getRoles().get(0).getRoleName()).build())
                .build();
        userToAssignRemove = UserEntity.builder()
                .email("assign@email.com")
                .role(RoleEntity.builder()
                        .roleName(RoleEnum.CUSTOMER.getRoleName()).build())
                .build();

        createTicketRequest = CreateTicketRequest.builder()
                .criticalLevel(CriticalLevelEnum.HIGH.getName())
                .description("test-desc").title("ticket")
                .projectId(project.getId())
                .build();

        updateTicketRequest = UpdateTicketRequest.builder()
                .ticketId(ticket.getId()).reproductionSteps("rep-tests")
                .description("no desc").status(TicketStatusEnum.VERIFIED.getName())
                .build();
        assignRemoveTicketRequest = AssignRemoveTicketRequest.builder()
                .email(userToAssignRemove.getEmail())
                .ticketId(ticket.getId())
                .build();
    }

    @DisplayName("Get Ticket Tests")
    @Nested
    class GetTicketTests {
        @Test
        public void getTicketTests_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenReturn(project);

            CommonResponse<TicketEntity> response =
                    ticketService.getTicket(TicketServiceTest.this.ticket.getId());

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertEquals(ticket, response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void getTicketTests_DbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenThrow(new NotFoundException(""));
            when(projectProxyService.getProject(any(Long.class)))
                    .thenReturn(project);

            assertThrows(
                    NotFoundException.class,
                    () -> ticketService.getTicket(TicketServiceTest.this.ticket.getId())
            );
        }

        @Test
        public void getTicketTests_ProjectServiceException_FailToConnect_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () -> ticketService.getTicket(TicketServiceTest.this.ticket.getId())
            );
        }

        @Test
        public void getTicketTests_ProjectServiceException_NotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenThrow(new NotFoundException(""));

            assertThrows(
                    NotFoundException.class,
                    () -> ticketService.getTicket(TicketServiceTest.this.ticket.getId())
            );
        }

        @Test
        public void getTicketTests_UserNotAssignedToProject_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            project.setAssignedPms(new HashSet<>());
            when(projectProxyService.getProject(any(Long.class)))
                    .thenReturn(project);

            assertThrows(
                    InvalidOperationException.class,
                    () -> ticketService.getTicket(TicketServiceTest.this.ticket.getId())
            );
        }
    }

    @DisplayName("Create Ticket Tests")
    @Nested
    class CreateTicketTests {
        @Test
        public void createTicket_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenReturn(project);
            TicketEntity t = TicketUtils.buildTicket(createTicketRequest,
                    user, project);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            CommonResponse<TicketEntity> response =
                    ticketService.createTicket(createTicketRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertEquals(t, response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void createTicket_ProjectService_FailedToConnect_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenThrow(new RuntimeException(""));
            TicketEntity t = TicketUtils.buildTicket(createTicketRequest,
                    user, project);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    RuntimeException.class,
                    () -> ticketService.createTicket(createTicketRequest)
            );
        }

        @Test
        public void createTicket_ProjectService_NotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenThrow(new NotFoundException(""));
            TicketEntity t = TicketUtils.buildTicket(createTicketRequest,
                    user, project);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    NotFoundException.class,
                    () -> ticketService.createTicket(createTicketRequest)
            );
        }

        @Test
        public void createTicket_UserNoAssignedToProject_Invalid() {
            when(userService.getUser()).thenReturn(user);
            project.setAssignedPms(new HashSet<>());
            when(projectProxyService.getProject(any(Long.class)))
                    .thenReturn(project);
            TicketEntity t = TicketUtils.buildTicket(createTicketRequest,
                    user, project);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    InvalidOperationException.class,
                    () -> ticketService.createTicket(createTicketRequest)
            );
        }

        @Test
        public void createTicket_DbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(projectProxyService.getProject(any(Long.class)))
                    .thenReturn(project);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () -> ticketService.createTicket(createTicketRequest)
            );
        }
    }

    @DisplayName("Update Ticket Tests")
    @Nested
    class UpdateTicketTests {
        @Test
        public void updateTicket_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(ticket);

            CommonResponse<TicketEntity> response = ticketService.updateTicket(updateTicketRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void updateTicket_TicketNotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenThrow(new NotFoundException(""));
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(ticket);

            assertThrows(
                    NotFoundException.class,
                    () -> ticketService.updateTicket(updateTicketRequest)
            );
        }

        @Test
        public void updateTicket_UserNotAssignedToTicket_Invalid() {
            when(userService.getUser()).thenReturn(user);
            ticket.setAssignedPersonnel(new HashSet<>());
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(ticket);

            assertThrows(
                    InvalidOperationException.class,
                    () -> ticketService.updateTicket(updateTicketRequest)
            );
        }

        @Test
        public void updateTicket_DbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () -> ticketService.updateTicket(updateTicketRequest)
            );
        }
    }

    @DisplayName("Delete Ticket Tests")
    @Nested
    class DeleteTicketTests {
        @Test
        public void deleteTicket_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);

            CommonResponse<TicketEntity> response = ticketService.deleteTicket(1L);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertEquals(ticket, response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void deleteTicket_TicketNotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenThrow(new NotFoundException(""));

            assertThrows(
                    NotFoundException.class,
                    () -> ticketService.deleteTicket(1L)
            );
        }

        @Test
        public void deleteTicket_UserNotAssigned_Invalid() {
            when(userService.getUser()).thenReturn(user);
            ticket.setAssignedPersonnel(new HashSet<>());
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);

            assertThrows(
                    InvalidOperationException.class,
                    () -> ticketService.deleteTicket(1L)
            );
        }
    }

    @DisplayName("Assign To Ticket Tests")
    @Nested
    class AssignToTicketTests {
        @Test
        public void assignToTicket_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            TicketEntity t = TicketUtils.assignToTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            CommonResponse<TicketEntity> response =
                    ticketService.assignToTicket(assignRemoveTicketRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertEquals(t, response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void assignToTicket_TicketNotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenThrow(new NotFoundException(""));
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            TicketEntity t = TicketUtils.assignToTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    NotFoundException.class,
                    () ->  ticketService.assignToTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void assignToTicket_AuthUserNotAssigned_Invalid() {
            when(userService.getUser()).thenReturn(user);
            ticket.setAssignedPersonnel(new HashSet<>());
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            TicketEntity t = TicketUtils.assignToTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    InvalidOperationException.class,
                    () ->  ticketService.assignToTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void assignToTicket_UserServiceException_FailedToConnect_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenThrow(new RuntimeException(""));
            TicketEntity t = TicketUtils.assignToTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    RuntimeException.class,
                    () ->  ticketService.assignToTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void assignToTicket_UserServiceException_userNotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenThrow(new NotFoundException(""));
            TicketEntity t = TicketUtils.assignToTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    NotFoundException.class,
                    () ->  ticketService.assignToTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void assignToTicket_DbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () ->  ticketService.assignToTicket(assignRemoveTicketRequest)
            );
        }
    }

    @DisplayName("Remove From Ticket Tests")
    @Nested
    class RemoveFromTicketTests {
        @Test
        public void removeFromTicket_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            TicketEntity t = TicketUtils.removeFromTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            CommonResponse<TicketEntity> response =
                    ticketService.removeFromTicket(assignRemoveTicketRequest);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertEquals(t, response.getPayload());
            assertNull(response.getError());
        }

        @Test
        public void removeFromTicket_TicketNotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenThrow(new NotFoundException(""));
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            TicketEntity t = TicketUtils.removeFromTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    NotFoundException.class,
                    () ->  ticketService.removeFromTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void removeFromTicket_AuthUserNotAssigned_Invalid() {
            when(userService.getUser()).thenReturn(user);
            ticket.setAssignedPersonnel(new HashSet<>());
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            TicketEntity t = TicketUtils.removeFromTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    InvalidOperationException.class,
                    () ->  ticketService.removeFromTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void removeFromTicket_UserServiceException_FailedToConnect_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenThrow(new RuntimeException(""));
            TicketEntity t = TicketUtils.removeFromTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    RuntimeException.class,
                    () ->  ticketService.removeFromTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void removeFromTicket_UserServiceException_userNotFound_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenThrow(new NotFoundException(""));
            TicketEntity t = TicketUtils.removeFromTicket(ticket, userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenReturn(t);

            assertThrows(
                    NotFoundException.class,
                    () ->  ticketService.removeFromTicket(assignRemoveTicketRequest)
            );
        }

        @Test
        public void removeFromTicket_DbServiceException_Invalid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.getTicket(any(Long.class)))
                    .thenReturn(ticket);
            when(userService.getUserDetails(any(GetUserDetailsRequest.class)))
                    .thenReturn(userToAssignRemove);
            when(ticketDbService.saveTicket(any(TicketEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () ->  ticketService.removeFromTicket(assignRemoveTicketRequest)
            );
        }
    }

    @DisplayName(("Find Tickets By Project ID Tests"))
    @Nested
    class FindTicketsByProjectIdTests{
        @Test
        public void findTicketsByProjectId_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.findTicketsByProjectId(any(Long.class)))
                    .thenReturn(Arrays.asList(ticket, ticket2));

            CommonResponse<List<TicketEntity>> response =
                    ticketService.getTicketByProjectId(1L);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertEquals(2, response.getPayload().size());
            assertNull(response.getError());
        }

        @Test
        public void findTicketsByProjectId_NoTicketsFound_Valid() {
            when(userService.getUser()).thenReturn(user);
            when(ticketDbService.findTicketsByProjectId(any(Long.class)))
                    .thenReturn(new ArrayList<>());

            CommonResponse<List<TicketEntity>> response =
                    ticketService.getTicketByProjectId(1L);

            assertEquals(HttpStatus.OK.value(), response.getStatus());
            assertTrue(response.isSuccess());
            assertNotNull(response.getPayload());
            assertEquals(0, response.getPayload().size());
            assertNull(response.getError());
        }
    }
}