package com.benzaied.gestiondestock.config;

import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsForClientService;
import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsService;
import com.benzaied.gestiondestock.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApplicationRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final ApplicationUserDetailsService userDetailsService;

    //private final ApplicationUserDetailsForClientService applicationUserDetailsForClientService;

    @Autowired
    public ApplicationRequestFilter(JwtUtil jwtUtil, ApplicationUserDetailsService userDetailsService/*, ApplicationUserDetailsForClientService applicationUserDetailsForClientService*/) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        //this.applicationUserDetailsForClientService = applicationUserDetailsForClientService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String userEmail = null;
        String jwt = null;
        String idEntreprise = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            userEmail = jwtUtil.extractUsername(jwt);
            idEntreprise = jwtUtil.extractIdEntreprise(jwt);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            /*if (userDetails == null){
                userDetails = this.applicationUserDetailsForClientService.loadUserByUsername(userEmail);
            }*/
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        MDC.put("idEntreprise", idEntreprise);
        chain.doFilter(request, response);
    }
}
