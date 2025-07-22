package com.example.fcmtokenapp;

public class LoginRequest {
    public String userId;
    public String token;

    public LoginRequest(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}

