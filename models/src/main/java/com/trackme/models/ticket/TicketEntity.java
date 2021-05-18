package com.trackme.models.ticket;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketEntity that = (TicketEntity) o;
        return Objects.equals(id, that.id)
                && projectId.equals(that.projectId)
                && title.equals(that.title)
                && status.equals(that.status)
                && criticalLevel.equals(that.criticalLevel)
                && Objects.equals(description, that.description)
                && Objects.equals(reproductionSteps, that.reproductionSteps)
                && Objects.equals(assignedPersonnel, that.assignedPersonnel)
                && Objects.equals(openedBy, that.openedBy)
                && Objects.equals(closedBy, that.closedBy)
                && Objects.equals(openedDate, that.openedDate)
                && Objects.equals(updatedDate, that.updatedDate)
                && Objects.equals(closedDate, that.closedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, title, status,
                criticalLevel, description, reproductionSteps,
                assignedPersonnel, openedBy, closedBy, openedDate,
                updatedDate, closedDate);
    }

    @Override
    public String toString() {
        return "TicketEntity{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", criticalLevel='" + criticalLevel + '\'' +
                ", description='" + description + '\'' +
                ", reproductionSteps='" + reproductionSteps + '\'' +
                ", assignedPersonnel=" + assignedPersonnel +
                ", openedBy=" + openedBy +
                ", closedBy=" + closedBy +
                ", openedDate=" + openedDate +
                ", updatedDate=" + updatedDate +
                ", closedDate=" + closedDate +
                '}';
    }
}
