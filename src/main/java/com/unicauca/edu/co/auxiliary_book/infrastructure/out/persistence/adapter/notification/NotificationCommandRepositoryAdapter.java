package com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.adapter.notification;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;
import com.unicauca.edu.co.auxiliary_book.domain.ports.notification.INotificationCommandRepositoryPort;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.entity.notification.NotificationEntity;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.mapper.notification.INotificationPersistenceMapper;
import com.unicauca.edu.co.auxiliary_book.infrastructure.out.persistence.repository.notification.INotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificationCommandRepositoryAdapter implements INotificationCommandRepositoryPort {

    private final INotificationRepository notificationRepository;
    private final INotificationPersistenceMapper notificationPersistenceMapper;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = notificationPersistenceMapper.toEntity(notification);
        NotificationEntity saved = notificationRepository.save(entity);
        return notificationPersistenceMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId, String userId) {
        notificationRepository.markAsRead(notificationId, userId, Instant.now());
    }

    @Override
    @Transactional
    public void markAllAsRead(String userId) {
        notificationRepository.markAllAsRead(userId, Instant.now());
    }

    @Override
    @Transactional
    public void deleteByIdAndUserId(UUID notificationId, String userId) {
        notificationRepository.deleteByNotificationIdAndUserId(notificationId, userId);
    }

    @Override
    @Transactional
    public void deleteAllReadByUserId(String userId) {
        notificationRepository.deleteAllReadByUserId(userId);
    }
}
