package com.hei.course.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityBeansConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.GET, "/ping")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/users")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/projections")
                    .permitAll()
                    .requestMatchers(HttpMethod.PUT, "/movies")
                    .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/projection")
                    .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.GET, "/reservations")
                    .hasAnyRole("EMPLOYEE", "MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/reservation")
                    .hasAnyRole("EMPLOYEE", "MANAGER")
                    .requestMatchers(HttpMethod.GET, "/projection")
                    .permitAll()
                    .requestMatchers(HttpMethod.PUT, "/movies")
                    .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/projection")
                    .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.GET, "/reservationById")
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .httpBasic(withDefaults());
    return http.build();
  }
}
