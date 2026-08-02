package com.hei.course.mapper;

import com.hei.course.entity.JUser;
import com.hei.course.model.User;

public class UserMapper {

  private UserMapper() {}

  public static User toModel(JUser entity) {
    if (entity == null) {
      return null;
    }

    User user = new User();
    user.setId(entity.getId());
    user.setFirstname(entity.getFirstname());
    user.setLastname(entity.getLastname());
    user.setBirthdate(entity.getBirthdate());
    user.setEmail(entity.getEmail());
    user.setPhone(entity.getPhone());
    user.setPassword(entity.getPassword());
    user.setUserRole(entity.getUserRole());

    return user;
  }

  public static JUser toEntity(User model) {
    if (model == null) {
      return null;
    }

    return JUser.builder()
        .id(model.getId())
        .firstname(model.getFirstname())
        .lastname(model.getLastname())
        .birthdate(model.getBirthdate())
        .email(model.getEmail())
        .phone(model.getPhone())
        .password(model.getPassword())
        .userRole(model.getUserRole())
        .build();
  }
}
