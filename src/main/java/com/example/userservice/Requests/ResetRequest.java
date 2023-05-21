package com.example.userservice.Requests;

import lombok.Data;

@Data
public class ResetRequest {
    private String token;
    private String password;
}
