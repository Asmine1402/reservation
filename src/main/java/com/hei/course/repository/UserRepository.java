package com.hei.course.repository;

import com.hei.course.entity.JUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JUser, UUID> {
  Optional<JUser> findByEmail(String email);

  boolean existsByEmail(String email);
}
