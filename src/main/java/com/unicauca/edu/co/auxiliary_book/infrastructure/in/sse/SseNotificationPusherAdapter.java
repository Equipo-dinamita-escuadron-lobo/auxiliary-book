package com.unicauca.edu.co.auxiliary_book.infrastructure.in.sse;

import com.unicauca.edu.co.auxiliary_book.application.ports.out.INotificationPusherPort;
import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.notification.NotificationResponse;
import com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.mapper.INotificationRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @brief Adaptador SSE para empujar notificaciones en tiempo real.
 */
@Component
@RequiredArgsConstructor
public class SseNotificationPusherAdapter implements INotificationPusherPort {

    private final SseEmitterRegistry sseEmitterRegistry;
    private final INotificationRestMapper notificationRestMapper;

    @Override
    public void push(String userId, Notification notification) {
        if (userId == null || notification == null) {
            return;
        }
        NotificationResponse payload = notificationRestMapper.toResponse(notification);
        sseEmitterRegistry.sendToUser(userId, "notification", payload);
    }
}
