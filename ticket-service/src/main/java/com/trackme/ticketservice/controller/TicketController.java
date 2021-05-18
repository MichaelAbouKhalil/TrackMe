package com.trackme.ticketservice.controller;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.ticket.AssignRemoveTicketRequest;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.security.permission.PmCustPermission;
import com.trackme.models.security.permission.PmDevCustPermission;
import com.trackme.models.security.permission.PmPermission;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/ticket/{id}")
    @PmDevCustPermission
    public ResponseEntity<CommonResponse<TicketEntity>> getTicket(@PathVariable("id") Long id) {
        log.info("received request on getTicket()");
        CommonResponse<TicketEntity> response = ticketService.getTicket(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/ticket")
    @PmDevCustPermission
    public ResponseEntity<CommonResponse<TicketEntity>> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        log.info("received request on createTicket()");
        CommonResponse<TicketEntity> response = ticketService.createTicket(request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/ticket")
    @PmDevCustPermission
    public ResponseEntity<CommonResponse<TicketEntity>> updateTicket(@Valid @RequestBody UpdateTicketRequest request) {
        log.info("received request on updateTicket()");
        CommonResponse<TicketEntity> response = ticketService.updateTicket(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/ticket/{id}")
    @PmCustPermission
    public ResponseEntity<CommonResponse<TicketEntity>> deleteTicket(@PathVariable("id") Long id) {
        log.info("received request on deleteTicket()");
        CommonResponse<TicketEntity> response = ticketService.deleteTicket(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/ticket/assign")
    @PmPermission
    public ResponseEntity<CommonResponse<TicketEntity>> assignToTicket(
            @Valid @RequestBody AssignRemoveTicketRequest request) {
        log.info("received request on assignToTicket()");
        CommonResponse<TicketEntity> response = ticketService.assignToTicket(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/ticket/remove")
    @PmPermission
    public ResponseEntity<CommonResponse<TicketEntity>> removeFromTicket(
            @Valid @RequestBody AssignRemoveTicketRequest request) {
        log.info("received request on removeFromTicket()");
        CommonResponse<TicketEntity> response = ticketService.removeFromTicket(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/ticket/project/{projectId}")
    @PmDevCustPermission
    public ResponseEntity<CommonResponse<List<TicketEntity>>> getTicketsByProject
            (@PathVariable("projectId") Long projectId) {
        log.info("received request on getTicketsByProject()");
        CommonResponse<List<TicketEntity>> response = ticketService.getTicketByProjectId(projectId);
        return ResponseEntity.ok().body(response);
    }
}
