package com.hei.course.repository;

import com.hei.course.entity.JProjection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectionRepository extends JpaRepository<JProjection, UUID> {
  List<JProjection> findByMovie_Id(UUID movieId);

  List<JProjection> findByRoom_Id(UUID roomId);
}
