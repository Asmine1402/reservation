package com.hei.course.repository;

import com.hei.course.entity.JReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository  extends JpaRepository<JReservation, UUID> {
    List<JReservation> findByUser_Id(UUID userId);
    boolean existsByIdAndUser_Id(UUID reservationId, UUID userId);
}




