package com.example.fcmtokenapp;

public class NotificationRequest {
    public String compte1;
    public String compte2;
    public String somme;

    public NotificationRequest(String compte1, String compte2, String somme, String token) {
        this.compte1 = compte1;
        this.compte2 = compte2;
        this.somme = somme;
    }
}
