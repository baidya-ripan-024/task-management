package com.organize.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/**
 * Filter that validates JWT token on each request.
 *
 * This filter extracts JWT token from the Authorization header and validates it.
 * If the token is valid, it populates the authentication context with the user
 * details and granted authorities. If the token is invalid, it throws a BadCredentialsException.
 *
 * Filter is only invoked once per request, hence the name "OncePerRequestFilter".
 */
@Slf4j
public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve JWT token from the Authorization header
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        /**
         * Validate the format of the JWT token.
         * If the token is not null, it should start with 'Bearer '.
         * If it doesn't, log a warning and throw an exception.
         */
        if (jwt != null) {
            if (jwt.startsWith("Bearer ")) {
                // Remove 'Bearer ' prefix from the token
                jwt = jwt.substring(7);
            } else {
                log.warn("Invalid token format! JWT token should start with 'Bearer '");
                throw new BadCredentialsException("Invalid token format!");
            }

            try {
                /**
                 * Parse the JWT token and extract the claims (email and authorities)
                 *
                 * Use the signing key to verify the signature of the token
                 * Extract the claims (email and authorities) from the token
                 */
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                // Extract email and authorities from the claims
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert authorities string to GrantedAuthority list
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                // Set the authentication object in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("JWT token validated successfully for email: {}", email);
            } catch (Exception e) {
                /**
                 * Log the error message and throw an exception if token validation fails
                 */
                log.error("Invalid token!", e);
                throw new BadCredentialsException("Invalid token!");
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}