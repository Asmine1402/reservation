package com.hei.course.service;

import com.hei.course.entity.JUser;
import com.hei.course.mapper.UserMapper;
import com.hei.course.model.User;
import com.hei.course.repository.UserRepository;
import com.hei.course.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    JUser entity =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

    User user = UserMapper.toModel(entity);

    return new UserPrincipal(user);
  }
}
