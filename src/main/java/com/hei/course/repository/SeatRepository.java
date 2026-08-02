package com.hei.course.repository;

import com.hei.course.entity.JSeat;
import com.hei.course.entity.JUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository extends JpaRepository<JSeat, UUID> {
    List<JSeat> findByRoom_Id(UUID roomId);

}

