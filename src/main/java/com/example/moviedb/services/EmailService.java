package com.example.moviedb.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
