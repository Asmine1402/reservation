package com.hei.course.repository;

import com.hei.course.entity.JRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<JRoom, UUID> {
}
