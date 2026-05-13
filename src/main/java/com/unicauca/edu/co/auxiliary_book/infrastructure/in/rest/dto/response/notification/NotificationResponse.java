package com.unicauca.edu.co.auxiliary_book.infrastructure.in.rest.dto.response.notification;

import com.unicauca.edu.co.auxiliary_book.domain.models.enums.ENotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private UUID notificationId;
    private String userId;
    private ENotificationType type;
    private String title;
    private String message;
    private UUID referenceId;
    private String referencePublicId;
    private boolean read;
    private Instant createdAt;
    private Instant readAt;
}
