package com.prachi18.college_management_system.Filters;

import com.prachi18.college_management_system.Entities.User;
import com.prachi18.college_management_system.Services.AuthService;
import com.prachi18.college_management_system.Services.CustomUserDetailsService;
import com.prachi18.college_management_system.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestHeaderToken = request.getHeader("Authorization");

        if (requestHeaderToken != null && requestHeaderToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = requestHeaderToken.split("Bearer ")[1];

       try {
           long userId = jwtService.getUserIdFromJwtToken(token);

           if (SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails user = customUserDetailsService.loadUserById(userId);
               UsernamePasswordAuthenticationToken authenticationToken =
                       new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

               authenticationToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
               log.info("Authentication stored: {}",
                       SecurityContextHolder.getContext().getAuthentication());
           }
       }
       catch (Exception e) {
                log.error("Invalid Jwt token ", e);
       }

        filterChain.doFilter(request, response);
    }
}
