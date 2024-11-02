package com.LibraryManagementSystem.borrowingRecord.service;

import com.LibraryManagementSystem.book.model.Book;
import com.LibraryManagementSystem.book.repository.BookRepository;
import com.LibraryManagementSystem.borrowingRecord.Enum.BorrowingStatus;
import com.LibraryManagementSystem.borrowingRecord.model.BorrowingRecord;
import com.LibraryManagementSystem.borrowingRecord.repository.BorrowingRecordRepository;
import com.LibraryManagementSystem.borrowingRecord.request.FinishBorrowingRequest;
import com.LibraryManagementSystem.borrowingRecord.request.NewBorrowingRecordRequest;
import com.LibraryManagementSystem.borrowingRecord.response.BorrowingRecordResponse;
import com.LibraryManagementSystem.borrowingRecord.response.BorrowingRecordResponseWrapper;
import com.LibraryManagementSystem.patron.model.Patron;
import com.LibraryManagementSystem.patron.repository.PatronRepository;
import com.LibraryManagementSystem.utils.Mapper.ClassMapper;
import com.LibraryManagementSystem.utils.Validator.ObjectsValidator;
import com.LibraryManagementSystem.utils.exception.RequestNotValidException;
import com.LibraryManagementSystem.utils.request.PaginationRequest;
import com.LibraryManagementSystem.utils.response.PaginationResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BorrowingRecordService {
    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;
    @Autowired
    private ObjectsValidator<NewBorrowingRecordRequest> borrowingRecordRequestObjectsValidator;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PatronRepository patronRepository;

    @Transactional
    public BorrowingRecordResponse borrowBook(UUID bookId, UUID patronId, NewBorrowingRecordRequest request) {
        borrowingRecordRequestObjectsValidator.validate(request);
        Book demandedBook = bookRepository.findById(bookId).orElseThrow(
                ()-> new RequestNotValidException("there is no book with this id: " + bookId)
        );
        Patron patron = patronRepository.findById(patronId).orElseThrow(
                ()-> new RequestNotValidException("there is no patron with id: " + patronId)
        );
        BorrowingRecord borrowingRecord = ClassMapper.INSTANCE.borrowingRecordDtoToEntity(request);
        borrowingRecord.setBook(demandedBook);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setStatus(BorrowingStatus.Borrowed);
        borrowingRecordRepository.save(borrowingRecord);
        BorrowingRecordResponse response = ClassMapper.INSTANCE.entityToDto(borrowingRecord);
        response.setBookId(borrowingRecord.getBook().getId());
        response.setPatronId(borrowingRecord.getPatron().getId());
        return response;
    }

    @Transactional
    public BorrowingRecordResponse returnBook(UUID bookId, UUID patronId, FinishBorrowingRequest request) {
        Book demandedBook = bookRepository.findById(bookId).orElseThrow(
                ()-> new RequestNotValidException("there is no book with this id: " + bookId)
        );
        Patron patron = patronRepository.findById(patronId).orElseThrow(
                ()-> new RequestNotValidException("there is no patron with id: " + patronId)
        );
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNullAndStatusIs(demandedBook,patron,BorrowingStatus.Borrowed).orElseThrow(
                ()-> new RequestNotValidException("There is no active borrowing record for book id: " + demandedBook.getId() +" from patron: " + patronId)
        );
        borrowingRecord.setStatus(BorrowingStatus.Returned);
        borrowingRecord.setReturnDate(request.getReturnDate());
        borrowingRecordRepository.save(borrowingRecord);
        BorrowingRecordResponse response = ClassMapper.INSTANCE.entityToDto(borrowingRecord);
        response.setBookId(borrowingRecord.getBook().getId());
        response.setPatronId(borrowingRecord.getPatron().getId());
        return response;
    }
}
