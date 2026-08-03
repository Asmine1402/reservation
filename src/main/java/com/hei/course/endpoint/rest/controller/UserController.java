package com.hei.course.endpoint.rest.controller;

import com.hei.course.endpoint.rest.dto.UserInput;
import com.hei.course.model.User;
import com.hei.course.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody UserInput request) {
    User user =
        userService.createUser(
            request.firstname(),
            request.lastname(),
            request.birthdate(),
            request.email(),
            request.phone(),
            request.password(),
            request.userRole());
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }
}
