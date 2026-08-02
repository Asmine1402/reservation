package com.hei.course.repository;

import com.hei.course.entity.JRoom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<JRoom, UUID> {}
