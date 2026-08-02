package com.hei.course.repository;

import com.hei.course.entity.JMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<JMovie, UUID> {
}
