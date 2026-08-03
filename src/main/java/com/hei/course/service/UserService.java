package com.hei.course.service;

import com.hei.course.entity.JUser;
import com.hei.course.mapper.UserMapper;
import com.hei.course.model.User;
import com.hei.course.model.UserRole;
import com.hei.course.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUser(
      String firstname,
      String lastname,
      LocalDateTime birthdate,
      String email,
      String phone,
      String rawPassword,
      UserRole userRole) {

    User user = new User();
    user.setFirstname(firstname);
    user.setLastname(lastname);
    user.setBirthdate(birthdate);
    user.setEmail(email);
    user.setPhone(phone);
    user.setPassword(passwordEncoder.encode(rawPassword));
    user.setUserRole(userRole);

    JUser entityToSave = UserMapper.toEntity(user);
    JUser savedEntity = userRepository.save(entityToSave);

    return UserMapper.toModel(savedEntity);
  }
}
