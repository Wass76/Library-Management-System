package com.BaseProject.user.repository;

import com.BaseProject.user.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,Integer> {
    Optional<Manager> findByEmail(String email);
}
