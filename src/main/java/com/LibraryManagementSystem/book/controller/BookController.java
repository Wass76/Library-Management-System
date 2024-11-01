package com.LibraryManagementSystem.book.controller;

import com.LibraryManagementSystem.book.request.BookRequest;
import com.LibraryManagementSystem.book.response.BookInfoResponse;
import com.LibraryManagementSystem.book.response.BookInfoResponseWrapper;
import com.LibraryManagementSystem.book.service.BookService;
import com.LibraryManagementSystem.utils.controller.BaseController;
import com.LibraryManagementSystem.utils.exception.ConflictException;
import com.LibraryManagementSystem.utils.request.PaginationRequest;
import com.LibraryManagementSystem.utils.response.ApiResponseClass;
import com.LibraryManagementSystem.utils.response.PaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "Manage book resources")
public class BookController extends BaseController {

    @Autowired
    private BookService bookService;

    @Operation(
            summary = "Get All Books",
            description = "Retrieve a paginated list of all books.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved books",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Get all books",
                          "status": "OK",
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "body": {
                              "books": [
                                  {
                                      "id": "e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                                      "title": "Spring in Action",
                                      "author": "Craig Walls",
                                      "isbn": "9781617294945",
                                      "publicationYear": "2020-06-01",
                                      "publisher": "Manning",
                                      "category": "Programming",
                                      "language": "English",
                                      "numberOfPages": 520,
                                      "dateAdded": "2023-11-01",
                                      "dateModified": "2023-11-01"
                                  }
                              ],
                              "pagination": {
                                  "page": 1,
                                  "perPage": 10,
                                  "total": 100
                              }
                          }
                        }
                        """)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllBooks(@RequestParam int page, @RequestParam int size) {
        PaginationRequest paginationRequest = PaginationRequest.builder()
                .size(size)
                .page(page)
                .build();
        BookInfoResponseWrapper books = bookService.getAllBooks(paginationRequest);
        return super.sendResponse(books.getBookInfo() , "Get all books" , HttpStatus.OK , books.getPagination());
    }

    @Operation(
            summary = "Get Book by ID",
            description = "Retrieve a book by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved book",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Get book by id",
                          "status": "OK",
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "body": {
                              "id": "e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                              "title": "Spring in Action",
                              "author": "Craig Walls",
                              "isbn": "9781617294945",
                              "publicationYear": "2020-06-01",
                              "publisher": "Manning",
                              "category": "Programming",
                              "language": "English",
                              "numberOfPages": 520,
                              "dateAdded": "2023-11-01",
                              "dateModified": "2023-11-01"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Book not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257"
                          "message": "there is no book with id: e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<?> getBookById(@PathVariable UUID id) {
        BookInfoResponse book = bookService.getBookInfoById(id);
        return super.sendResponse(book,"Get book by id" , HttpStatus.OK );
    }

    @Operation(
            summary = "Add a New Book",
            description = "Add a new book to the library.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Add book",
                          "status": "OK",
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "body": {
                              "id": "e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                              "title": "Spring in Action",
                              "author": "Craig Walls",
                              "isbn": "9781617294945",
                              "publicationYear": "2020-06-01",
                              "publisher": "Manning",
                              "category": "Programming",
                              "language": "English",
                              "numberOfPages": 520,
                              "dateAdded": "2023-11-01",
                              "dateModified": "2023-11-01"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "message": "Validation failed for fields: title, author, isbn",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookRequest request) {
        BookInfoResponse book = bookService.addBook(request);
        return super.sendResponse(book,"Add book" , HttpStatus.OK );
    }

    @Operation(
            summary = "Update Book",
            description = "Update an existing book's details by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Update book by id",
                          "status": "OK",
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "body": {
                              "id": "e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                              "title": "Spring Boot Essentials",
                              "author": "Craig Walls",
                              "isbn": "9781617294945",
                              "publicationYear": "2021-07-01",
                              "publisher": "Manning",
                              "category": "Programming",
                              "language": "English",
                              "numberOfPages": 550,
                              "dateAdded": "2023-11-01",
                              "dateModified": "2024-11-01"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Book ID not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257"
                          "message": "there is no book with id: e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<?> updateBook(@PathVariable UUID id, @RequestBody BookRequest request) {
        BookInfoResponse bookInfoResponse = bookService.updateBook(request,id);
        return super.sendResponse(bookInfoResponse,"Update book by id" , HttpStatus.OK );
    }
    @Operation(
            summary = "Delete Book",
            description = "Delete a book from the library by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Delete book by id",
                          "status": "OK",
                          "localDateTime": "2024-11-01T00:36:20.0659257"
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Book ID not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "message": "there is no book with id: e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
      boolean isDeleted =  bookService.deleteBook(id);
      if(isDeleted){
          return super.sendResponse("Delete book by id" , HttpStatus.OK );
      }
      throw new ConflictException("something went wrong" );
    }
}
