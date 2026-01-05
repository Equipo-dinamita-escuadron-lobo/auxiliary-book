package com.unicauca.edu.co.auxiliary_book.domain.ports.notification;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.EmailMessage;

/**
 * Output port for sending emails.
 */
public interface IEmailNotificationPort {

    void sendEmail(EmailMessage emailMessage);
}
