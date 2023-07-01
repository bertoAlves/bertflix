package com.bertflix.authms.services;

public interface EmailSender {
    void send(String to, String email);
    void sendConfirmationEmail(String to, String name, String token);
}
