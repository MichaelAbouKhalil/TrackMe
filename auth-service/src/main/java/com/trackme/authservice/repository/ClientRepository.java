package com.trackme.authservice.repository;

import com.trackme.models.security.ClientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientDetailsEntity, String> {
}
