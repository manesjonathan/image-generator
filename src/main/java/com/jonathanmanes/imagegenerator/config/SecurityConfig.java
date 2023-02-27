package com.jonathanmanes.imagegenerator.config;

import com.jonathanmanes.imagegenerator.repository.UserRepository;
import com.jonathanmanes.imagegenerator.service.JwtUtils;
import com.jonathanmanes.imagegenerator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public JwtUtils jwtUtils(UserService userService) {
        return new JwtUtils(userService);
    }

    @Bean
    public UserService userService(PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtils jwtUtils) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/generate").hasRole("USER") // Add authorization rule for the /generate endpoint

                        .anyRequest().authenticated())
                .csrf().disable()
                .cors().and().addFilterBefore(new JwtAuthorizationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
