package com.hei.course.repository;

import com.hei.course.entity.JMovie;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<JMovie, UUID> {}
