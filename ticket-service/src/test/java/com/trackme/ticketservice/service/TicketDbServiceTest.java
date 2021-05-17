package com.trackme.ticketservice.service;

import com.trackme.models.enums.CriticalLevelEnum;
import com.trackme.models.exception.NotFoundException;
import com.trackme.models.ticket.TicketEntity;
import com.trackme.ticketservice.Base;
import com.trackme.ticketservice.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class TicketDbServiceTest extends Base {

    @Autowired
    TicketDbService ticketDbService;

    @MockBean
    TicketRepository ticketRepository;

    TicketEntity ticket;
    TicketEntity ticket2;
    List<TicketEntity> list;
    Long ticketId;

    @BeforeEach
    void setUp() {
        Mockito.reset(ticketRepository);
        ticket = TicketEntity.builder().id(1L).title("test-ticket")
                .criticalLevel(CriticalLevelEnum.HIGH.getName())
                .projectId(1L).build();
        ticket2 = TicketEntity.builder().id(1L).title("test-ticket-2")
                .criticalLevel(CriticalLevelEnum.LOW.getName())
                .projectId(1L).build();
        list = Arrays.asList(ticket, ticket2);
        ticketId = 1L;
    }

    @DisplayName("Get Ticket Tests")
    @Nested
    class GetTicketsTests {
        @Test
        public void getTicket_Found_Valid() {
            given(ticketRepository.findById(ticketId))
                    .willReturn(Optional.of(ticket));

            TicketEntity foundTicket = ticketDbService.getTicket(ticketId);

            assertEquals(ticket.getId(), foundTicket.getId());
            assertEquals(ticket.getTitle(), foundTicket.getTitle());
            assertEquals(ticket.getCriticalLevel(), foundTicket.getCriticalLevel());
            assertEquals(ticket.getProjectId(), foundTicket.getProjectId());
        }

        @Test
        public void getTicket_NotFound_Invalid() {
            when(ticketRepository.findById(any(Long.class)))
                    .thenThrow(new NotFoundException(""));

            assertThrows(
                    NotFoundException.class,
                    () -> ticketDbService.getTicket(ticketId)
            );
        }
    }

    @DisplayName("Save Ticket Tests")
    @Nested
    class SaveTicketTests {
        @Test
        public void saveTicket_Valid() {
            when(ticketRepository.save(any(TicketEntity.class)))
                    .thenReturn(ticket);

            TicketEntity savedTicket = ticketDbService.saveTicket(ticket);

            assertEquals(ticket.getId(), savedTicket.getId());
            assertEquals(ticket.getTitle(), savedTicket.getTitle());
            assertEquals(ticket.getCriticalLevel(), savedTicket.getCriticalLevel());
            assertEquals(ticket.getProjectId(), savedTicket.getProjectId());
        }

        @Test
        public void saveTicket_Invalid() {
            when(ticketRepository.save(any(TicketEntity.class)))
                    .thenThrow(new RuntimeException(""));

            assertThrows(
                    RuntimeException.class,
                    () -> ticketDbService.saveTicket(ticket)
            );
        }
    }

    @DisplayName("Delete Ticket Tests")
    @Nested
    class DeleteTicketTests {
        @Test
        public void deleteTicket_Valid() {
            doNothing().when(ticketRepository).deleteById(any(Long.class));

            ticketDbService.deleteTicket(ticketId);
        }
    }

    @DisplayName("Find Tickets By Project Tests")
    @Nested
    class FindTicketsByProjectTests {
        @Test
        public void findTicketsByProject_Valid() {
            when(ticketRepository.findByProjectId(any(Long.class)))
                    .thenReturn(Optional.of(list));

            List<TicketEntity> ticketsByProjectId =
                    ticketDbService.findTicketsByProjectId(1L);

            assertEquals(list.size(), ticketsByProjectId.size());
        }

        @Test
        public void findTicketsByProject_Invalid() {
            when(ticketRepository.findByProjectId(any(Long.class)))
                    .thenReturn(Optional.of(new ArrayList<>()));

            List<TicketEntity> ticketsByProjectId =
                    ticketDbService.findTicketsByProjectId(1L);

            assertEquals(0, ticketsByProjectId.size());
        }
    }

}