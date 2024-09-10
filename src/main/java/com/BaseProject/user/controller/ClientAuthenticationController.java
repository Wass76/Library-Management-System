package com.BaseProject.user.controller;

import com.BaseProject.user.response.ClientAuthenticationResponse;
import com.BaseProject.user.request.AuthenticationRequest;
import com.BaseProject.user.request.ChangePasswordRequest;
import com.BaseProject.user.request.ClientRegisterRequest;
import com.BaseProject.user.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/clientAuth")
@RequiredArgsConstructor
public class ClientAuthenticationController {

    private final ClientService clientAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<ClientAuthenticationResponse> register(
            @RequestBody ClientRegisterRequest request
    ){
       return ResponseEntity.ok(clientAuthenticationService.ClientRegister(request));
    }

    @Operation(
            description = "This endpoint to login client to his account",
            summary = "Login to our system",
            responses =
                @ApiResponse(
                        responseCode = "200",
                        description = "Done successfully"
                )
    )
    @PostMapping("/login")
    public ResponseEntity<ClientAuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request , HttpServletRequest httpServletRequest
    ){
       return ResponseEntity.ok(clientAuthenticationService.ClientLogin(request, httpServletRequest));
    }


    @Operation(
            description = "This endpoint build for make user able to change his password by enter old and new password",
            summary =  "Change password of user",
            responses ={
                    @ApiResponse(
                            description = "Change password done successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Wrong old password / Passwords are not the same",
                            responseCode = "403"
                    )
            }
    )
    @PutMapping("/change-password")
//    @PreAuthorize()
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    )
    {
        clientAuthenticationService.changePassword(request , connectedUser);
        return ResponseEntity.accepted().body("Change password done successfully");
    }



//    @PostMapping("/logout")
//    public ResponseEntity<AuthenticationResponse> logout(
//            @RequestBody AuthenticationRequest request
//    ){
//        return ResponseEntity.ok(authenticationService.authenticate(request));
//    }
}
