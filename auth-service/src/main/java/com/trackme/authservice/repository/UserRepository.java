package com.trackme.authservice.repository;

import com.trackme.models.security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findByUsername(String username);

    public Optional<UserEntity> findByEmail(String email);

}
