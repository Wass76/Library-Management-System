package com.LibraryManagementSystem.borrowingRecord.controller;

import com.LibraryManagementSystem.borrowingRecord.request.FinishBorrowingRequest;
import com.LibraryManagementSystem.borrowingRecord.request.NewBorrowingRecordRequest;
import com.LibraryManagementSystem.borrowingRecord.response.BorrowingRecordResponse;
import com.LibraryManagementSystem.borrowingRecord.service.BorrowingRecordService;
import com.LibraryManagementSystem.utils.controller.BaseController;
import com.LibraryManagementSystem.utils.response.ApiResponseClass;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
@Tag(name = "Borrowing Record Controller", description = "Manage borrowing and returning of books")
public class BorrowingRecordController extends BaseController {
    @Autowired
    private BorrowingRecordService borrowingRecordService;

    @Operation(
            summary = "Borrow a Book",
            description = "Allows a patron to borrow a book, recording the borrowing details.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book borrowed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Borrow a book",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00",
                          "body": {
                              "borrowDate": "2024-10-01",
                              "dueDate": "2024-10-15",
                              "status": "Borrowed",
                              "fineAmount": 0.0,
                              "patronId": "123e4567-e89b-12d3-a456-426614174000",
                              "bookId": "123e4567-e89b-12d3-a456-426614174111"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Book or patron not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "message": "there is no book with this id: 123e4567-e89b-12d3-a456-426614174000",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping("borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> borrowBook(@PathVariable UUID bookId, @PathVariable UUID patronId , NewBorrowingRecordRequest request) {
        BorrowingRecordResponse response = borrowingRecordService.borrowBook(bookId,patronId,request);
        return super.sendResponse(response,"Borrow a book" , HttpStatus.OK);
    }

    @Operation(
            summary = "Return a Book",
            description = "Allows a patron to return a book, updating the borrowing record with the return date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Return a book",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00",
                          "body": {
                              "borrowDate": "2024-10-01",
                              "dueDate": "2024-10-15",
                              "returnDate": "2024-10-14",
                              "status": "Returned",
                              "fineAmount": 0.0,
                              "patronId": "123e4567-e89b-12d3-a456-426614174000",
                              "bookId": "123e4567-e89b-12d3-a456-426614174111"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Active borrowing record not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "message": "There is no active borrowing record for book id: 123e4567-e89b-12d3-a456-426614174111 from patron: 123e4567-e89b-12d3-a456-426614174000",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @PutMapping("borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<?> returnBook(@PathVariable UUID bookId, @PathVariable UUID patronId , @RequestBody FinishBorrowingRequest request) {
        BorrowingRecordResponse response = borrowingRecordService.returnBook(bookId,patronId,request);
        return super.sendResponse(response,"Return a book" , HttpStatus.OK);
    }
}
