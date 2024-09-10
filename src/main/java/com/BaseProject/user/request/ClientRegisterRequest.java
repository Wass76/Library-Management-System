package com.BaseProject.user.request;


import com.BaseProject.utils.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientRegisterRequest {

    @NotBlank(message = "first name couldn't be blank")
    private String firstName;

    @NotBlank(message = "last name couldn't be blank")
    private String lastName;

    @NotBlank(message = "Email couldn't be blank")
    private String email;

    @NotBlank(message = "username couldn't be blank")
    private String username;

    @NotBlank(message = "Password couldn't be blank")
    @ValidPassword
    private String password;
}
