package com.BaseProject.user.repository;

import com.BaseProject.user.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Integer> {

    Optional<Client> findByEmail(String email);
    Optional<Client> findByUsername(String username);
}
