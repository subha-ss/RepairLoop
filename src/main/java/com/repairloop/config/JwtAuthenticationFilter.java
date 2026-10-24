
package com.repairloop.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Claims claims = jwtService.parseClaims(token);
                Long userId = Long.parseLong(claims.getSubject());
                String role = claims.get("role", String.class);

                if (role != null && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

                    var authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + role));

                    var authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId, null, authorities);

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            } catch (Exception exception) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"error\":\"Invalid or expired JWT token\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
