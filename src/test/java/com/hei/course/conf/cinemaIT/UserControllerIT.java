package com.hei.course.conf.cinemaIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hei.course.conf.FacadeIT;
import com.hei.course.endpoint.rest.dto.UserInput;
import com.hei.course.model.User;
import com.hei.course.model.UserRole;
import com.hei.course.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserControllerIT extends FacadeIT {

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
  }

  @Test
  void should_create_user_successfully() {
    UserInput input =
        new UserInput(
            "Fanja",
            "Rakoto",
            LocalDateTime.parse("1998-05-20T10:00:00"),
            "fanja@gmail.com",
            "0987654",
            "fanja123",
            UserRole.CLIENT);

    HttpEntity<UserInput> request = new HttpEntity<>(input);

    ResponseEntity<User> response =
        testRestTemplate.exchange("/users", HttpMethod.POST, request, User.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("fanja@gmail.com", response.getBody().getEmail());
    assertNotEquals("fanja123", response.getBody().getPassword());
  }
}
