package com.tickshow.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginAsAdminRequest {
    private String id;
    private String email;
    private String password;
}