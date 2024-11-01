package com.LibraryManagementSystem.book.repository;

import com.LibraryManagementSystem.book.model.Book;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Cacheable(value = "books", key = "#id")
    Optional<Book> findById(UUID id);

    @Cacheable(value = "books", key = "'allBooks'")
    Page<Book> findAll(Pageable pageable);

    @Cacheable(value = "books", key = "#title")
    Optional<Book> findByTitle(String title);

    @Cacheable(value = "books", key = "#isbn")
    Optional<Book> findByIsbn(String isbn);

    @Cacheable(value = "books", key = "#isbn + '_' + #uuid")
    Optional<Book> findByIsbnAndIdIsNot(String isbn, UUID uuid);

    @CacheEvict(value = "books", key = "'allBooks'")
    <S extends Book> S save(S entity);

    @CachePut(value = "books", key = "#entity.id")
    <S extends Book> S saveAndFlush(S entity);

    @CacheEvict(value = "books", key = "#id")
    void deleteById(UUID id);

    @CacheEvict(value = "books", allEntries = true)
    void deleteAll();
}
