package com.unicauca.edu.co.auxiliary_book.infrastructure.out.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * @brief Configuración de seguridad HTTP de la aplicación.
 *
 * Configura la cadena de filtros de Spring Security: deshabilita CSRF,
 * permite acceso público a los endpoints de Swagger y actuator, exige
 * autenticación para el resto, utiliza JWT (resource server) con el
 * {@link JwtAuthConverter} para convertir tokens y autoridades, y
 * establece sesión sin estado.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    /**
     * @brief Configura la cadena de filtros de seguridad.
     *
     * Deshabilita CSRF, abre al público Swagger y actuator, exige
     * autenticación en el resto, registra JWT como resource server
     * con el converter de autoridades y aplica política de sesión
     * STATELESS.
     *
     * @param httpSecurity Objeto HttpSecurity a configurar.
     * @return SecurityFilterChain configurado.
     * @throws Exception si ocurre un error al construir la configuración.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(http -> http
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/actuator/**").permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter));
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
