package com.LibraryManagementSystem.controller;

import com.LibraryManagementSystem.book.model.Book;
import com.LibraryManagementSystem.book.repository.BookRepository;
import com.LibraryManagementSystem.borrowingRecord.Enum.BorrowingStatus;
import com.LibraryManagementSystem.borrowingRecord.controller.BorrowingRecordController;
import com.LibraryManagementSystem.borrowingRecord.repository.BorrowingRecordRepository;
import com.LibraryManagementSystem.borrowingRecord.request.FinishBorrowingRequest;
import com.LibraryManagementSystem.borrowingRecord.request.NewBorrowingRecordRequest;
import com.LibraryManagementSystem.borrowingRecord.response.BorrowingRecordResponse;
import com.LibraryManagementSystem.borrowingRecord.service.BorrowingRecordService;
import com.LibraryManagementSystem.patron.model.Patron;
import com.LibraryManagementSystem.patron.repository.PatronRepository;
import com.LibraryManagementSystem.utils.exception.RequestNotValidException;
import com.LibraryManagementSystem.utils.response.ApiResponseClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordControllerTest {

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testBorrowBook_Success() {
        UUID bookId = UUID.randomUUID();
        UUID patronId = UUID.randomUUID();
        NewBorrowingRecordRequest request = new NewBorrowingRecordRequest();
        request.setBorrowDate(LocalDate.now());
        request.setDueDate(LocalDate.now().plusDays(14));

        BorrowingRecordResponse mockResponse = new BorrowingRecordResponse();
        mockResponse.setBorrowDate(request.getBorrowDate());
        mockResponse.setDueDate(request.getDueDate());
        mockResponse.setStatus("Borrowed");
        mockResponse.setFineAmount(0.0);
        mockResponse.setPatronId(patronId);
        mockResponse.setBookId(bookId);

        ApiResponseClass expectedResponse = ApiResponseClass.builder()
                        .message("Borrow a book")
                                .status(HttpStatus.OK)
                                        .body(mockResponse)
                                                .build();

        when(borrowingRecordService.borrowBook(bookId, patronId, request)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = borrowingRecordController.borrowBook(bookId, patronId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponseClass actualResponse = (ApiResponseClass) responseEntity.getBody();
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
    }


    @Test
    void testReturnBook_Success() {
        UUID bookId = UUID.randomUUID();
        UUID patronId = UUID.randomUUID();
        FinishBorrowingRequest request = new FinishBorrowingRequest();
        request.setReturnDate(LocalDate.now());

        BorrowingRecordResponse mockResponse = new BorrowingRecordResponse();
        mockResponse.setBorrowDate(LocalDate.now().minusDays(14));
        mockResponse.setDueDate(LocalDate.now());
        mockResponse.setReturnDate(request.getReturnDate());
        mockResponse.setStatus("Returned");
        mockResponse.setFineAmount(0.0);
        mockResponse.setPatronId(patronId);
        mockResponse.setBookId(bookId);

        ApiResponseClass expectedResponse = ApiResponseClass.builder()
                        .message("Return a book")
                                .status(HttpStatus.OK)
                                        .body(mockResponse)
                                                .build();

        when(borrowingRecordService.returnBook(bookId, patronId, request)).thenReturn(mockResponse);

        ResponseEntity<?> responseEntity = borrowingRecordController.returnBook(bookId, patronId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponseClass actualResponse = (ApiResponseClass) responseEntity.getBody();

        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
        assertEquals(expectedResponse.getBody(), actualResponse.getBody());

        verify(borrowingRecordService, times(1)).returnBook(bookId, patronId, request);
    }

}
