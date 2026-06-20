package com.unicauca.edu.co.auxiliary_book.application.useCase.notification;

import com.unicauca.edu.co.auxiliary_book.application.ports.in.notification.INotificationQueryPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;
import com.unicauca.edu.co.auxiliary_book.domain.ports.notification.INotificationQueryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryUC implements INotificationQueryPort {

    private final INotificationQueryRepositoryPort notificationQueryRepositoryPort;

    @Override
    public List<Notification> list(String userId, Boolean unreadOnly, Integer limit) {
        return notificationQueryRepositoryPort.findByUserId(userId, unreadOnly, limit);
    }

    @Override
    public long countUnread(String userId) {
        return notificationQueryRepositoryPort.countUnread(userId);
    }
}
