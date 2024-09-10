package com.BaseProject.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientAuthenticationResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String token;

}