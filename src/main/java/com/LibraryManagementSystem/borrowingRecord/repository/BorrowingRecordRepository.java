package com.LibraryManagementSystem.borrowingRecord.repository;

import com.LibraryManagementSystem.book.model.Book;
import com.LibraryManagementSystem.borrowingRecord.Enum.BorrowingStatus;
import com.LibraryManagementSystem.borrowingRecord.model.BorrowingRecord;
import com.LibraryManagementSystem.patron.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, UUID> {

    Optional<BorrowingRecord> findBorrowingRecordByBookAndPatronAndReturnDateIsNullAndStatusIs(Book book, Patron patron, BorrowingStatus status);
}
