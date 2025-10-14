package com.unicauca.edu.co.auxiliary_book.infrastructure.out.security;

import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @brief Converter for JWT authentication tokens.
 *
 * Converts a {@link Jwt} into an {@link AbstractAuthenticationToken} for use in authentication,
 * extracting authorities and user information from the JWT claims.
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken>, IJwtUtils {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAtrribute;

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    Jwt jwtToken;

    /**
     * @brief Converts a JWT into an {@link AbstractAuthenticationToken} for authentication.
     * @param jwt The JWT to convert.
     * @return The corresponding {@link AbstractAuthenticationToken}.
     */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream
                .concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRoles(jwt).stream())
                .toList();

        this.jwtToken = jwt;

        return new JwtAuthenticationToken(jwt, authorities, getPrincipleName(jwt));
    }

    /**
     * @brief Retrieves the principal name from the JWT.
     *
     * By default, uses the "sub" claim, but can be configured to use a different claim.
     *
     * @param jwt The JWT from which to extract the principal name.
     * @return The authenticated user's principal name.
     */
    private String getPrincipleName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;

        if (principleAtrribute != null) {
            claimName = principleAtrribute;
        }

        return jwt.getClaim(claimName);
    }

    /**
     * @brief Extracts roles from the "resource_access" claim in the JWT for the configured resource ID.
     *
     * Converts each role into a {@link SimpleGrantedAuthority} with the "ROLE_" prefix.
     * Returns an empty collection if the claim or roles are not present.
     *
     * @param jwt The JWT from which to extract roles.
     * @return A collection of {@link GrantedAuthority} representing the roles.
     */
    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if (jwt.getClaim("resource_access") == null) {
            return List.of();
        }

        resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess.get(resourceId) == null) {
            return List.of();
        }

        resource = (Map<String, Object>) resourceAccess.get(resourceId);

        if (resource.get("roles") == null) {
            return List.of();
        }

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
                .toList();
    }

    /**
     * @brief Retrieves the "sub" claim value from the JWT, used as the user identifier.
     * @return The identifier of the authenticated user.
     */
    @Override
    public String getId() {
        return (String) jwtToken.getClaims().get("sub");
    }

}
