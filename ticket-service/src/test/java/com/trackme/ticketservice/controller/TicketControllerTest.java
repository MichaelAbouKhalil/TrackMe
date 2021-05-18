package com.trackme.ticketservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.enums.CriticalLevelEnum;
import com.trackme.models.enums.TicketStatusEnum;
import com.trackme.models.payload.request.ticket.AssignRemoveTicketRequest;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TicketControllerTest extends BaseController {

    private static final String BASE_API = "/ticket/";

    @MockBean
    TicketService ticketService;

    TicketEntity ticket;
    CreateTicketRequest createTicketRequest;
    UpdateTicketRequest updateTicketRequest;
    AssignRemoveTicketRequest assignRemoveTicketRequest;

    @BeforeEach
    void setUp() {
        ticket = TicketEntity.builder().title("test-ticket")
                .criticalLevel(CriticalLevelEnum.MEDIUM.getName())
                .description("desc").id(1L).build();

        createTicketRequest = CreateTicketRequest.builder()
                .projectId(1L).title("test-ticket")
                .criticalLevel(CriticalLevelEnum.LOW.getName())
                .build();

        updateTicketRequest = UpdateTicketRequest.builder()
                .ticketId(1L).criticalLevel(CriticalLevelEnum.HIGH.getName())
                .description("test-desc").reproductionSteps("rest-rep")
                .status(TicketStatusEnum.VERIFIED.getName()).build();

        assignRemoveTicketRequest = AssignRemoveTicketRequest.builder()
                .ticketId(1L).email("pm@test.com").build();
    }

    @DisplayName("Request Validation Tests")
    @Nested
    class RequestValidationTests {
        @Test
        public void getTicket_ValidRequest_Valid() throws Exception {
            when(ticketService.getTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(get(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void deleteTicket_InvalidRequest_Invalid() throws Exception {
            when(ticketService.deleteTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(delete(BASE_API + null)
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createTicket_ValidRequest_Valid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void createTicket_InvalidRequest_Invalid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            createTicketRequest.setCriticalLevel("loww");

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void updateTicket_ValidRequest_Valid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void updateTicket_InvalidRequest_Invalid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            updateTicketRequest.setStatus("hello");

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void assignToTicket_ValidRequest_Valid() throws Exception {
            when(ticketService.assignToTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "assign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void removeAssigneeFromTicket_InvalidRequest_Invalid() throws Exception {
            when(ticketService.removeFromTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            assignRemoveTicketRequest.setEmail("hello@bye");

            mockMvc.perform(post(BASE_API + "remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Get Ticket Access Tests")
    @Nested
    class GetTicketAccessTests {
        @Test
        public void getTicket_NoAuth_Invalid() throws Exception {
            when(ticketService.getTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(get(BASE_API + ticket.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void getTicket_Admin_Invalid() throws Exception {
            when(ticketService.getTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(get(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void getTicket_Pm_Valid() throws Exception {
            when(ticketService.getTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(get(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void getTicket_Dev_Valid() throws Exception {
            when(ticketService.getTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(get(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void getTicket_Cust_Valid() throws Exception {
            when(ticketService.getTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(get(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
    }

    @DisplayName("Create Ticket Access Tests")
    @Nested
    class CreateTicketAccessTests {
        @Test
        public void createTicket_NoAuth_Invalid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createTicket_Admin_Invalid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void createTicket_Pm_Valid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void createTicket_Dev_Valid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void createTicket_Cust_Valid() throws Exception {
            when(ticketService.createTicket(any(CreateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
    }

    @DisplayName("Update Ticket Access Tests")
    @Nested
    class UpdateTicketAccessTests {
        @Test
        public void updateTicket_NoAuth_Invalid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void updateTicket_Admin_Invalid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void updateTicket_Pm_Valid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void updateTicket_Dev_Valid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void updateTicket_Cust_Valid() throws Exception {
            when(ticketService.updateTicket(any(UpdateTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(put(BASE_API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
    }

    @DisplayName("Delete Ticket Access Tests")
    @Nested
    class DeleteTicketAccessTests {
        @Test
        public void deleteTicket_NoAuth_Invalid() throws Exception {
            when(ticketService.deleteTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(delete(BASE_API + ticket.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void deleteTicket_Admin_InValid() throws Exception {
            when(ticketService.deleteTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(delete(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void deleteTicket_Pm_Valid() throws Exception {
            when(ticketService.deleteTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(delete(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void deleteTicket_Dev_Invalid() throws Exception {
            when(ticketService.deleteTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(delete(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void deleteTicket_Cust_Valid() throws Exception {
            when(ticketService.deleteTicket(any(Long.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(delete(BASE_API + ticket.getId())
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
    }

    @DisplayName("Assign To Ticket Access Tests")
    @Nested
    class AssignToTicketAccessTests {
        @Test
        public void assignToTicket_NoAuth_Invalid() throws Exception {
            when(ticketService.assignToTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "assign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void assignToTicket_Admin_Invalid() throws Exception {
            when(ticketService.assignToTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "assign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void assignToTicket_Pm_Valid() throws Exception {
            when(ticketService.assignToTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "assign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void assignToTicket_Dev_Invalid() throws Exception {
            when(ticketService.assignToTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "assign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void assignToTicket_Cust_Invalid() throws Exception {
            when(ticketService.assignToTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "assign")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Remove From Ticket Access Tests")
    @Nested
    class RemoveFromTicketAccessTests {
        @Test
        public void removeFromTicket_NoAuth_Invalid() throws Exception {
            when(ticketService.removeFromTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void removeFromTicket_Admin_Invalid() throws Exception {
            when(ticketService.removeFromTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void removeFromTicket_Pm_Valid() throws Exception {
            when(ticketService.removeFromTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void removeFromTicket_Dev_Invalid() throws Exception {
            when(ticketService.removeFromTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void removeFromTicket_Cust_Invalid() throws Exception {
            when(ticketService.removeFromTicket(any(AssignRemoveTicketRequest.class)))
                    .thenReturn(CommonResponse.ok(ticket));

            mockMvc.perform(post(BASE_API + "remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(assignRemoveTicketRequest))
                    .header("AUTHORIZATION", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }
    }

    @DisplayName("Find Tickets By Project ID Access Tests")
    @Nested
    class FindTicketsByProjectIdAccessTests {
        @Test
        public void findTicketsByProjectId_NoAuth_Invalid() throws Exception {
            when(ticketService.getTicketByProjectId(any(Long.class)))
                    .thenReturn(CommonResponse.ok(Arrays.asList(ticket)));

            mockMvc.perform(get(BASE_API + "/project/" + 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void findTicketsByProjectId_Admin_Invalid() throws Exception {
            when(ticketService.getTicketByProjectId(any(Long.class)))
                    .thenReturn(CommonResponse.ok(Arrays.asList(ticket)));

            mockMvc.perform(get(BASE_API + "/project/" + 1L)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                    .andExpect(jsonPath("$.payload").doesNotExist())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.error.errorMessage").exists());
        }

        @Test
        public void findTicketsByProjectId_Pm_Valid() throws Exception {
            when(ticketService.getTicketByProjectId(any(Long.class)))
                    .thenReturn(CommonResponse.ok(Arrays.asList(ticket)));

            mockMvc.perform(get(BASE_API + "/project/" + 1L)
                    .header("Authorization", "Bearer " + pmToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void findTicketsByProjectId_Dev_Valid() throws Exception {
            when(ticketService.getTicketByProjectId(any(Long.class)))
                    .thenReturn(CommonResponse.ok(Arrays.asList(ticket)));

            mockMvc.perform(get(BASE_API + "/project/" + 1L)
                    .header("Authorization", "Bearer " + devToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }

        @Test
        public void findTicketsByProjectId_Customer_Valid() throws Exception {
            when(ticketService.getTicketByProjectId(any(Long.class)))
                    .thenReturn(CommonResponse.ok(Arrays.asList(ticket)));

            mockMvc.perform(get(BASE_API + "/project/" + 1L)
                    .header("Authorization", "Bearer " + custToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                    .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                    .andExpect(jsonPath("$.payload").exists())
                    .andExpect(jsonPath("$.error").doesNotExist())
                    .andExpect(jsonPath("$.error.errorMessage").doesNotExist());
        }
    }
}