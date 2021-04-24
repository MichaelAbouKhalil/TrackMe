package com.trackme.models.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "client_details")
public class ClientDetailsEntity {

    @Id
    @Column(name = "client_id")
    private String clientId;

    @Column(name = "clientSecret")
    private String clientSecret;

    @Column(name = "authorizedGrantTypes")
    private String authorizedGrantTypes;
}
