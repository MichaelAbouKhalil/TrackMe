package com.trackme.models.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "authorities",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"permission"})})
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission", nullable = false)
    private String permission;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToMany(mappedBy = "authorities")
    @JsonIgnore
    private List<RoleEntity> roles;
}
