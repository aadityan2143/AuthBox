package com.applogin.login.repository;

import com.applogin.login.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email); //Optional<T> -  (or Spring Data MongoDB) repository method declaration that searches for a user by their email address and safely handles the possibility that no user is found.
    Boolean existsByEmail(String email);
}
