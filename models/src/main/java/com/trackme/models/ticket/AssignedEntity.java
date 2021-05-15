package com.trackme.models.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_assigned_personnel")
@JsonIgnoreProperties(value = {"ticket", "opened", "closed"})
public class AssignedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private TicketEntity ticket;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opened_ticket_id")
    private TicketEntity opened;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_ticket_id")
    private TicketEntity closed;

}
