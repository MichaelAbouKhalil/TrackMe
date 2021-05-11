package com.trackme.models.project;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name = "assigned_pms", joinColumns = @JoinColumn(name = "project_id"))
    @Singular
    private Set<String> assignedPms = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "assigned_devs", joinColumns = @JoinColumn(name = "project_id"))
    @Singular
    private Set<String> assignedDevs = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "assigned_custs", joinColumns = @JoinColumn(name = "project_id"))
    @Singular
    private Set<String> assignedCustomers = new HashSet<>();
}
