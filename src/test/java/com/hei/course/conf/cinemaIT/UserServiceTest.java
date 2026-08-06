package com.hei.course.conf.cinemaIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hei.course.entity.JUser;
import com.hei.course.model.User;
import com.hei.course.model.UserRole;
import com.hei.course.repository.UserRepository;
import com.hei.course.service.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private PasswordEncoder passwordEncoder;

  private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository, passwordEncoder);
  }

  @Test
  void should_create_user_with_encoded_password() {
    when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

    JUser savedEntity =
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

    when(userRepository.save(any(JUser.class))).thenReturn(savedEntity);

    User result =
        userService.createUser(
            "Asmine",
            "Razafy",
            LocalDateTime.parse("2003-02-14T14:30:00"),
            "asmine@gmail.com",
            "0123456",
            "plainPassword",
            UserRole.CLIENT);

    assertEquals("Asmine", result.getFirstname());
    assertEquals("asmine@gmail.com", result.getEmail());
    assertEquals("encodedPassword", result.getPassword());
    assertEquals(UserRole.CLIENT, result.getUserRole());
  }
}
