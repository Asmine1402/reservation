package com.hei.course.repository;

import com.hei.course.entity.JUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<JUser, UUID> {
   Optional<JUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
