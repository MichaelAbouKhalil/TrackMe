package com.trackme.models.ticket;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "tbl_tickets")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "critical_level", nullable = false)
    private String criticalLevel;

    @Column(name = "decription")
    private String description;

    @Column(name = "reproduction_steps")
    private String reproductionSteps;

    @Singular("assign")
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Set<AssignedEntity> assignedPersonnel;

    @OneToOne(mappedBy = "opened", cascade = CascadeType.ALL)
    private AssignedEntity openedBy;

    @OneToOne(mappedBy = "closed", cascade = CascadeType.ALL)
    private AssignedEntity closedBy;

    @CreationTimestamp
    @Column(name = "opened_date")
    private LocalDateTime openedDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

}
