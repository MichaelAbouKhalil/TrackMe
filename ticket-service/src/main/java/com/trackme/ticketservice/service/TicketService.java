package com.trackme.ticketservice.service;

import com.trackme.models.common.CommonResponse;
import com.trackme.models.payload.request.ticket.AssignRemoveTicketRequest;
import com.trackme.models.payload.request.ticket.CreateTicketRequest;
import com.trackme.models.payload.request.ticket.UpdateTicketRequest;
import com.trackme.models.ticket.TicketEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketDbService ticketDbService;

    public CommonResponse<TicketEntity> getTicket(Long id) {
        return null;
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
