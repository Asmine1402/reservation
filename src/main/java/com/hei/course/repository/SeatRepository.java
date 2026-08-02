package com.hei.course.repository;

import com.hei.course.entity.JSeat;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<JSeat, UUID> {
  List<JSeat> findByRoom_Id(UUID roomId);
}
