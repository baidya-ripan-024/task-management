package com.organize.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class provides methods for generating and parsing JWT tokens.
 */
public class JwtProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

    // Secret key used for signing the JWT tokens
    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    /**
     * Generates a JWT token based on the given authentication object.
     *
     * @param authentication the authentication object to generate the JWT token for
     * @return the generated JWT token
     */
    public static String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86_400_000)) // 86400000 -> 24 hours
                .claim("email", authentication.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();

        log.info("Generated JWT token for user: {}", authentication.getName());
        return jwt;
    }

    /**
     * Extracts the email from a given JWT token.
     *
     * @param jwt the JWT token to extract the email from
     * @return the extracted email
     */
    public static String getEmailFromJwtToken(String jwt) {
        try {
            jwt = jwt.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String email = String.valueOf(claims.get("email"));
            log.info("Extracted email from JWT token: {}", email);
            return email;
        } catch (Exception e) {
            log.error("Failed to extract email from JWT token", e);
            return null;
        }
    }

    /**
     * Populates the authorities string with the given collection of authorities.
     *
     * @param authorities the collection of authorities to populate the string with
     * @return the populated authorities string
     */
    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auth = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auth.add(authority.getAuthority());
        }
        return String.join(",", auth);
    }
}