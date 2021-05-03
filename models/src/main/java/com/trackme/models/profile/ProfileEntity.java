package com.trackme.models.profile;

import com.trackme.models.security.UserEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "user_profile", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"user_name"})})
public class ProfileEntity {

    @Id
    @Column(name = "user_name")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Transient
    private UserEntity userEntity;

}
