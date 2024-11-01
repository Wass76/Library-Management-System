package com.LibraryManagementSystem.user.controller;

import com.LibraryManagementSystem.user.request.LibrarianRegisterRequest;
import com.LibraryManagementSystem.user.response.LibrarianAuthenticationResponse;
import com.LibraryManagementSystem.user.request.AuthenticationRequest;
import com.LibraryManagementSystem.user.request.ChangePasswordRequest;
import com.LibraryManagementSystem.user.service.LibrarianService;
import com.LibraryManagementSystem.utils.restExceptionHanding.ApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/librarianAuth")
@RequiredArgsConstructor
@Tag(name = "Librarian Authentication Controller", description = "Endpoints for librarian registration, login, and password management")
public class LibrarianAuthenticationController {

    private final LibrarianService librarianService;


    @Operation(
            summary = "Register a new librarian",
            description = "This endpoint allows new librarians to register an account by providing required details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details about librarian to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LibrarianRegisterRequest.class),
                            examples = @ExampleObject(
                                    """
                                              {
                                              "firstName": "Wassem",
                                              "lastName": "Tenbakji",
                                              "email": "wassem@gmail.com",
                                              "password": "Password!1",
                                              "role": "OWNER"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registration successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LibrarianAuthenticationResponse.class),
                                    examples = @ExampleObject("""
                        {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "firstName": "Wassem",
                          "lastName": "Tenbakji",
                          "email": "wassem.tenbakji@example.com",
                          "token": "eyJhbGciOiJIUzI1NiIsInR..."
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiException.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Email already exists",
                          "status": "BAD_REQUEST",
                          "localDateTime": "2024-11-01T21:55:21"
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<LibrarianAuthenticationResponse> register(
            @RequestBody LibrarianRegisterRequest request
    ){
       return ResponseEntity.ok(librarianService.librarianRegister(request));
    }

    @Operation(
            summary = "Login as librarian",
            description = "Allows librarians to log in using their email and password. Implements rate limiting to prevent abuse.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details about librarian to be created",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationRequest.class),
                            examples = @ExampleObject(
                                    """
                                              {
                                              "email": "wassem@gmail.com",
                                              "password": "Password!1"
                                              }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LibrarianAuthenticationResponse.class),
                                    examples = @ExampleObject("""
                        {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "firstName": "Wassem",
                          "lastName": "Tenbakji",
                          "email": "wassem.tenbakji@example.com",
                          "token": "eyJhbGciOiJIUzI1NiIsInR..."
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Invalid credentials or rate limit exceeded",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                        {
                          "error": "Forbidden",
                          "message": "Invalid email or password",
                          "status": 403
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LibrarianAuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request , HttpServletRequest httpServletRequest
    ){
       return ResponseEntity.ok(librarianService.librarianLogin(request, httpServletRequest));
    }

    @Operation(
            summary = "Change password",
            description = "Allows librarians to change their password by providing their current password, new password, and confirmation of the new password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "old and new password to be changed",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ChangePasswordRequest.class),
                            examples = @ExampleObject(
                                    """
                                            {
                                              "currentPassword": "Password!1",
                                              "newPassword": "Password!2",
                                              "confirmPassword": "Password!2"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password changed successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                        {
                          "message": "Change password done successfully",
                          "status": "OK",
                          "localDateTime": "2024-11-01T10:00:00"
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Wrong current password or new passwords do not match",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject("""
                        {
                          "error": "Forbidden",
                          "message": "Wrong password",
                          "status": 403
                        }
                        """)
                            )
                    )
            }
    )
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    )
    {
        librarianService.changePassword(request , connectedUser);
        return ResponseEntity.accepted().body("Change password done successfully");
    }
}
