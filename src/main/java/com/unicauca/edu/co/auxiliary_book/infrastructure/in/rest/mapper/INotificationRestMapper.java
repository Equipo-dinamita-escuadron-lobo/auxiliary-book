package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.notification.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class INotificationRestMapper {

    public NotificationResponse toResponse(Notification n) {
        if (n == null) return null;
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .userId(n.getUserId())
                .type(n.getType())
                .title(n.getTitle())
                .message(n.getMessage())
                .referenceId(n.getReferenceId())
                .referencePublicId(n.getReferencePublicId())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .build();
    }
}
