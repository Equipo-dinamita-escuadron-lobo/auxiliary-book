package com.unicauca.edu.co.auxiliary_book.infrastructure.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuración centralizada para WebClient y el cliente HTTP para el microservicio de Stock.
 */
@Configuration
public class WebClientConfig {

    /**
     * Crea un bean de WebClient.Builder que ya está preparado para el balanceo de carga.
     * La anotación @LoadBalanced es crucial para que Spring Cloud pueda resolver
     * los nombres de servicio registrados en Eureka (ej. "lb://STOCK").
     *
     * @return Un WebClient.Builder configurado.
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    /**
     * Define un filtro para WebClient que propaga el token JWT.
     * Este filtro se ejecutará en cada petición saliente.
     * Extrae el token JWT del contexto de seguridad de la petición entrante
     * y lo añade como un encabezado "Authorization" a la petición saliente.
     *
     * @return Un ExchangeFilterFunction que añade el header de autorización.
     */
    private ExchangeFilterFunction jwtPropagationFilter() {
        return (clientRequest, next) -> {
            // Obtiene la autenticación actual del contexto de seguridad
            var authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verifica si la autenticación es de tipo JWT
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                // Extrae el valor del token (el string)
                String tokenValue = jwtAuth.getToken().getTokenValue();

                // Clona la petición original y le añade el encabezado de autorización
                ClientRequest newRequest = ClientRequest.from(clientRequest)
                        .header("Authorization", "Bearer " + tokenValue)
                        .build();

                // Continúa la cadena de filtros con la nueva petición
                return next.exchange(newRequest);
            }

            // Si no hay token, continúa con la petición original
            return next.exchange(clientRequest);
        };
    }
}