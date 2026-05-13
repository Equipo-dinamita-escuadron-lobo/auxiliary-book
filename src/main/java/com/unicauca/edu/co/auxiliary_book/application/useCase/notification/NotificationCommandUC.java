package com.unicauca.edu.co.auxiliary_book.application.useCase.notification;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.notification.INotificationCommandPort;
import com.unicauca.edu.co.auxiliary_book.domain.ports.notification.INotificationCommandRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationCommandUC implements INotificationCommandPort {

    private final INotificationCommandRepositoryPort notificationCommandRepositoryPort;

    @Override
    public void markAsRead(UUID notificationId, String userId) {
        notificationCommandRepositoryPort.markAsRead(notificationId, userId);
    }

    @Override
    public void markAllAsRead(String userId) {
        notificationCommandRepositoryPort.markAllAsRead(userId);
    }

    @Override
    public void delete(UUID notificationId, String userId) {
        notificationCommandRepositoryPort.deleteByIdAndUserId(notificationId, userId);
    }

    @Override
    public void deleteAllRead(String userId) {
        notificationCommandRepositoryPort.deleteAllReadByUserId(userId);
    }
}
