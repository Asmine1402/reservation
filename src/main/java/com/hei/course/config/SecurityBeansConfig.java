package com.hei.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // GET /projections -> 200 pour tout le monde (spec: "Public / All roles")
                        .requestMatchers(HttpMethod.GET, "/projections").permitAll()

                        // PUT /movies -> 403 CLIENT/EMPLOYEE, 200 MANAGER uniquement
                        .requestMatchers(HttpMethod.PUT, "/movies").hasRole("MANAGER")

                        // PUT /projection -> 403 CLIENT/EMPLOYEE, 200 MANAGER uniquement
                        .requestMatchers(HttpMethod.PUT, "/projection").hasRole("MANAGER")

                        // GET /reservations -> 403 CLIENT, 200 EMPLOYEE/MANAGER
                        .requestMatchers(HttpMethod.GET, "/reservations").hasAnyRole("EMPLOYEE", "MANAGER")

                        // PUT /reservation -> 403 CLIENT, 200 EMPLOYEE/MANAGER
                        .requestMatchers(HttpMethod.PUT, "/reservation").hasAnyRole("EMPLOYEE", "MANAGER")

                        // GET /reservationById -> regle "CLIENT voit seulement SA reservation"
                        // depend de la donnee (id du proprietaire), pas juste du role : on ne peut
                        // pas l'exprimer avec un requestMatcher base sur l'URL. On laisse donc
                        // passer tout utilisateur authentifie ici, et c'est au controller/service
                        // de comparer reservation.getUser().getId() avec l'utilisateur connecte
                        // et de renvoyer 403 lui-meme si ce n'est pas sa reservation
                        // (voir JReservationRepository.existsByIdAndUser_Id).
                        .requestMatchers(HttpMethod.GET, "/reservationById").authenticated()

                        // Tout le reste : au minimum authentifie
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}