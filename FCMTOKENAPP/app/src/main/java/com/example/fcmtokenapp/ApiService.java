package com.example.fcmtokenapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/notifications/send-to-device")
    Call<Void> sendNotificationData(@Body NotificationRequest request);

    @POST("/login")
    Call<Void> login(@Body LoginRequest request);
}