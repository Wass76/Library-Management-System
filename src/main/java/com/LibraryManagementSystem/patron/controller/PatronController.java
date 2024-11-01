package com.LibraryManagementSystem.patron.controller;

import com.LibraryManagementSystem.patron.request.PatronRequest;
import com.LibraryManagementSystem.patron.response.PatronInfoResponse;
import com.LibraryManagementSystem.patron.response.PatronInfoResponseWrapper;
import com.LibraryManagementSystem.patron.service.PatronService;
import com.LibraryManagementSystem.utils.controller.BaseController;
import com.LibraryManagementSystem.utils.exception.ConflictException;
import com.LibraryManagementSystem.utils.request.PaginationRequest;
import com.LibraryManagementSystem.utils.response.ApiResponseClass;
import com.LibraryManagementSystem.utils.restExceptionHanding.ApiException;
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

import java.util.UUID;

@RestController
@RequestMapping("api/patrons")
@CrossOrigin("*")
@Tag(name = "Patron Controller", description = "Manage patron resources")
public class PatronController extends BaseController {
    @Autowired
    private PatronService patronService;
    @Operation(
            summary = "Get All Patrons",
            description = "Retrieve a paginated list of all patrons.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved patrons",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Get all patrons",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00",
                          "body": {
                              "patronsInfo": [
                                  {
                                      "id": "d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                                      "firstName": "Wassem",
                                      "lastName": "Tenbakji",
                                      "email": "wassem.tenbakji@example.com",
                                      "phoneNumber": "+963933693310",
                                      "address": "Damascus",
                                      "membershipDate": "2023-10-15"
                                  }
                              ],
                              "pagination": {
                                  "page": 1,
                                  "perPage": 10,
                                  "total": 50
                              }
                          }
                        }
                        """)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllPatrons(@RequestParam int page, @RequestParam int size) {
        PaginationRequest paginationRequest = PaginationRequest.builder()
                .size(size)
                .page(page)
                .build();
        PatronInfoResponseWrapper response = patronService.findAllPatrons(paginationRequest);
        return super.sendResponse(response.getPatronsInfo() , "Get all patrons" , HttpStatus.OK ,response.getPagination());
    }

    @Operation(
            summary = "Get Patron by ID",
            description = "Retrieve a patron by their ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved patron",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Get patron by id",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00",
                          "body": {
                              "id": "d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                              "firstName": "Wassem",
                              "lastName": "Tenbakji",
                              "email": "wassem.tenbakji@example.com",
                              "phoneNumber": "+963933693310",
                              "address": "Damascus",
                              "membershipDate": "2023-10-15"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Patron not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiException.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T10:00:00",
                          "message": "there is no patron with id: d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<?> getPatronById(@PathVariable UUID id) {
        PatronInfoResponse response = patronService.findPatronById(id);
        return super.sendResponse(response,"Get patron by id" , HttpStatus.OK);
    }

    @Operation(
            summary = "Add a New Patron",
            description = "Add a new patron to the library system.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patron added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Add patron",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00",
                          "body": {
                              "id": "d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                              "firstName": "Wassem",
                              "lastName": "Tenbakji",
                              "email": "wassem.tenbakji@example.com",
                              "phoneNumber": "+963933693310",
                              "address": "Damascus",
                              "membershipDate": "2023-10-15"
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
                          "localDateTime": "2024-11-01T10:00:00",
                          "message": "Validation failed for fields: firstName, email",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> addPatron(@RequestBody PatronRequest request) {
        PatronInfoResponse response = patronService.addPatron(request);
        return super.sendResponse(response,"Add patron" , HttpStatus.OK);
    }
    @Operation(
            summary = "Update Patron",
            description = "Update an existing patron's information by their ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patron updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Update patron by id",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00",
                          "body": {
                              "id": "d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                              "firstName": "Jane",
                              "lastName": "Smith",
                              "email": "jane.smith@example.com",
                              "phoneNumber": "+963933693310",
                              "address": "456 Elm St",
                              "membershipDate": "2023-10-15"
                          }
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Patron not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T10:00:00",
                          "message": "there is no patron with id: d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @PutMapping("{id}")
    public ResponseEntity<?> updatePatron(@PathVariable UUID id, @RequestBody PatronRequest request) {
        PatronInfoResponse response = patronService.updatePatron(request,id);
        return super.sendResponse(response,"Update patron" , HttpStatus.OK);
    }
    @Operation(
            summary = "Delete Patron",
            description = "Delete a patron from the library by their ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patron deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Delete patron",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00"
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Patron not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseClass.class),
                                    examples = @ExampleObject("""
                        {
                          "localDateTime": "2024-11-01T00:36:20.0659257",
                          "message": "there is no patron with id: d2c4feeb-5678-4a12-b3f8-1234abcd5678",
                          "status": 400
                        }
                        """)
                            )
                    )
            }
    )
    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePatron(@PathVariable UUID id) {
        boolean result = patronService.deletePatron(id);
        if (result) {
            return super.sendResponse("Delete patron" , HttpStatus.OK);
        }
        throw new ConflictException("something went wrong");
    }
}
