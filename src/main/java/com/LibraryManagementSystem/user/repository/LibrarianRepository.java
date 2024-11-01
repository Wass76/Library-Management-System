package com.LibraryManagementSystem.user.repository;

import com.LibraryManagementSystem.user.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibrarianRepository extends JpaRepository<Librarian,Integer> {
    Optional<Librarian> findByEmail(String email);
}
