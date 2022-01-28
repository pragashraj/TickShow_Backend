package com.tickshow.backend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String username;
    private String email;
    private String token;
    private int expirationInMilliseconds;
}