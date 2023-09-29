package com.benzaied.gestiondestock.config;

import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsForClientService;
import com.benzaied.gestiondestock.services.auth.ApplicationUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {


    //private final JwtAuthentificationFilter jwtAuthFilter;
    //private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    //private final ApplicationUserDetailsForClientService applicationUserDetailsForClientService;
    private final ApplicationRequestFilter applicationRequestFilter;

    /*@Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(applicationUserDetailsService)
                .passwordEncoder(passwordEncoder())
        ;
    }*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(corsFilter() , SessionManagementFilter.class)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/gestiondestock/v1/auth/**").permitAll()
                        .requestMatchers("/gestiondestock/v1/entreprises/create").permitAll()
                        //.requestMatchers("/gestiondestock/v1/ventes/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()
                        .requestMatchers("/v2/api-docs").permitAll()
                        .requestMatchers("/api-docs").permitAll()
                        .requestMatchers("/api-docs.json").permitAll()
                        .requestMatchers("/swagger-resources").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/configuration/ui").permitAll()
                        .requestMatchers("/configuration/security").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html").permitAll()






                        /*.requestMatchers("/gestiondestock/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())

                        .requestMatchers(GET, "/gestiondestock/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                        .requestMatchers(POST, "/gestiondestock/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                        .requestMatchers(PUT, "/gestiondestock/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                        .requestMatchers(DELETE, "/gestiondestock/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())*/

                        /*.requestMatchers("/gestiondestock/v1/admin/**").hasRole(ADMIN.name())

                        .requestMatchers(GET, "/gestiondestock/v1/admin/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST, "/gestiondestock/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/gestiondestock/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/gestiondestock/v1/admin/**").hasAuthority(ADMIN_DELETE.name())*/

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(applicationRequestFilter , UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(jwtAuthFilter , UsernamePasswordAuthenticationFilter.class)
                .logout(log -> log
                        .logoutUrl("/gestiondestock/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //if(applicationUserDetailsService != null){
        authProvider.setUserDetailsService(applicationUserDetailsService);
        /*} else{
            authProvider.setUserDetailsService(applicationUserDetailsForClientService);
        }*/
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    /*@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Replace with your frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

}
