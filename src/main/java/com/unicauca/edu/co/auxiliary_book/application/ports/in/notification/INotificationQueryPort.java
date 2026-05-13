package com.unicauca.edu.co.auxiliary_book.application.ports.in.notification;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;

import java.util.List;

/**
 * @brief Puerto de entrada de consulta de notificaciones.
 */
public interface INotificationQueryPort {
    List<Notification> list(String userId, Boolean unreadOnly, Integer limit);
    long countUnread(String userId);
}
