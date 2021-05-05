package com.trackme.models.project;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "project_tbl", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "org_id", nullable = false)
    private String ordId;

    @OneToMany(mappedBy = "project")
    @Singular
    private List<AssignedPmEntity> assignedPms;

    @OneToMany(mappedBy = "project")
    @Singular
    private List<AssignedDevEntity> assignedDevs;

    @OneToMany(mappedBy = "project")
    @Singular
    private List<AssignedCustEntity> assignedCustomers;
}
