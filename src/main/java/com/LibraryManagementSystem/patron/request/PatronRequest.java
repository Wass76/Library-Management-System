package com.LibraryManagementSystem.patron.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatronRequest {

    @NotBlank(message = "First name couldn't be blank")
    private String firstName;

    @NotBlank(message = "Last name couldn't be blank")
    private String lastName;

    @NotBlank(message = "Email couldn't be blank")
    private String email;

    @NotBlank(message = "Phone number shouldn't be blank")
    private String phoneNumber;

    @NotBlank(message = "address shouldn't be blank")
    private String address;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate membershipDate;
}
