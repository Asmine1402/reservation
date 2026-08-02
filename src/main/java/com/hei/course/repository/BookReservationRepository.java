package com.hei.course.repository;

import com.hei.course.entity.JBookReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookReservationRepository  extends JpaRepository<JBookReservation, UUID> {
    List<JBookReservation> findByReservation_Id(UUID reservationId);
    boolean existsBySeat_IdAndReservation_Id(UUID seatId, UUID reservationId);

}
