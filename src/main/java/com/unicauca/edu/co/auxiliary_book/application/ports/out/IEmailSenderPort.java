package com.unicauca.edu.co.auxiliary_book.application.ports.out;

public interface IEmailSenderPort {
    void sendReport(String to, String subject, String body, byte[] attachment, String fileName);
}
