package com.trackme.ticketservice.service;

import com.trackme.models.exception.NotFoundException;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketDbService {

    private final TicketRepository ticketRepository;

    public TicketEntity getTicket(Long id){
        log.info("finding ticket for id [{}]", id);
        TicketEntity ticket = ticketRepository.findById(id).orElseThrow(
                () -> new NotFoundException("ticket with id [" + id + "] is not found")
        );

        log.info("ticket with id [{}] found, title [{}]", ticket.getId(), ticket.getTitle());
        return ticket;
    }

    public TicketEntity saveTicket(TicketEntity ticket){
        log.info("saving ticket with title [{}]", ticket.getTitle());

        TicketEntity saved = ticketRepository.save(ticket);

        if(saved == null){
            throw new RuntimeException("error saving ticket to database");
        }

        return saved;
    }

    public void deleteTicket(Long id){
        log.info("deleting ticket with id [{}]", id);
        ticketRepository.deleteById(id);
        log.info("ticket with id [{}] has been deleted.", id);
    }

    public List<TicketEntity> findTicketsByProjectId(Long projectId){
        log.info("finding tickets for project id [{}]", projectId);

        Optional<List<TicketEntity>> optional =
                ticketRepository.findByProjectId(projectId);

        List<TicketEntity> tickets;
        if(optional.isPresent()){
            tickets = optional.get();
        }else{
            tickets = new ArrayList<>();
        }
        log.info("found [{}] tickets for project [{}]", tickets.size(), projectId);
        return tickets;
    }
}
