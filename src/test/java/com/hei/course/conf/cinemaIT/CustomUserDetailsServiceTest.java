package com.hei.course.conf.cinemaIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.hei.course.entity.JUser;
import com.hei.course.model.UserRole;
import com.hei.course.repository.UserRepository;
import com.hei.course.security.UserPrincipal;
import com.hei.course.service.CustomUserDetailsService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class CustomUserDetailsServiceTest {

  @Mock private UserRepository userRepository;

  private CustomUserDetailsService customUserDetailsService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    customUserDetailsService = new CustomUserDetailsService(userRepository);
  }

  @Test
  void should_load_user_when_email_exists() {
    JUser entity =
        JUser.builder()
            .id(UUID.randomUUID())
            .firstname("Asmine")
            .lastname("Razafy")
            .birthdate(LocalDateTime.parse("2003-02-14T14:30:00"))
            .email("asmine@gmail.com")
            .phone("0123456")
            .password("encodedPassword")
            .userRole(UserRole.CLIENT)
            .build();

    when(userRepository.findByEmail("asmine@gmail.com")).thenReturn(Optional.of(entity));

    UserDetails result = customUserDetailsService.loadUserByUsername("asmine@gmail.com");

    assertEquals("asmine@gmail.com", result.getUsername());
    assertEquals("ROLE_CLIENT", result.getAuthorities().iterator().next().getAuthority());
    assertEquals(UserPrincipal.class, result.getClass());
  }

  @Test
  void should_throw_when_email_not_found() {
    when(userRepository.findByEmail("unknown@gmail.com")).thenReturn(Optional.empty());

    assertThrows(
        UsernameNotFoundException.class,
        () -> customUserDetailsService.loadUserByUsername("unknown@gmail.com"));
  }
}
