package com.LibraryManagementSystem.user.request;


import com.LibraryManagementSystem.user.Enum.Role;
import com.LibraryManagementSystem.utils.annotation.ValidEnum;
import com.LibraryManagementSystem.utils.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianRegisterRequest {

    @NotBlank(message = "first name couldn't be blank")
    private String firstName;

    @NotBlank(message = "last name couldn't be blank")
    private String lastName;

    @NotBlank(message = "Email couldn't be blank")
    private String email;

    @NotBlank(message = "Password couldn't be blank")
    @ValidPassword
    private String password;

    @ValidEnum(enumClass = Role.class)
    private String role;
}
