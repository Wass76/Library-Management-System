package com.LibraryManagementSystem.patron.repository;

import com.LibraryManagementSystem.patron.model.Patron;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface PatronRepository extends JpaRepository<Patron, UUID> {

    @Cacheable(value = "patrons", key = "#id")
    Optional<Patron> findById(UUID id);

    @Cacheable(value = "patrons", key = "'allPatrons'")
    Page<Patron> findAll(Pageable pageable);

    @Cacheable(value = "patrons", key = "#email")
    Optional<Patron> findByEmail(String email);

    @Cacheable(value = "patrons", key = "#email + '_' + #uuid")
    Optional<Patron> findByEmailAndIdIsNot(String email, UUID uuid);

    @Cacheable(value = "patrons", key = "#phone")
    Optional<Patron> findByPhoneNumber(String phone);

    @Cacheable(value = "patrons", key = "#phone + '_' + #uuid")
    Optional<Patron> findByPhoneNumberAndIdIsNot(String phone, UUID uuid);

    @CacheEvict(value = "patrons", key = "'allPatrons'")
    <S extends Patron> S save(S entity);

    @CachePut(value = "patrons", key = "#entity.id")
    <S extends Patron> S saveAndFlush(S entity);

    @CacheEvict(value = "patrons", key = "#id")
    void deleteById(UUID id);

    @CacheEvict(value = "patrons", allEntries = true)
    void deleteAll();
}
