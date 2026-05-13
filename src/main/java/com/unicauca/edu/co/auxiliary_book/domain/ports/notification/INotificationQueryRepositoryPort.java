package com.unicauca.edu.co.auxiliary_book.domain.ports.notification;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @brief Puerto de lectura para notificaciones.
 */
public interface INotificationQueryRepositoryPort {
    List<Notification> findByUserId(String userId, Boolean unreadOnly, Integer limit);
    long countUnread(String userId);
    Optional<Notification> findByIdAndUserId(UUID notificationId, String userId);
}
