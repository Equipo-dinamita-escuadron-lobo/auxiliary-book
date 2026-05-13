package com.unicauca.edu.co.auxiliary_book.application.ports.out;

import com.unicauca.edu.co.auxiliary_book.domain.models.notification.Notification;

/**
 * @brief Puerto de salida para enviar notificaciones en tiempo real al usuario
 *        a traves de un canal de transporte (SSE/WebSocket/...).
 *
 * El adaptador correspondiente decide como entregar el mensaje. Si el
 * usuario no esta conectado el envio puede ser un no-op silencioso.
 */
public interface INotificationPusherPort {
    void push(String userId, Notification notification);
}
