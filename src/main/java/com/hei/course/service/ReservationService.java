package com.hei.course.service;

import com.hei.course.endpoint.rest.dto.ReservationInput;
import com.hei.course.entity.JProjection;
import com.hei.course.entity.JReservation;
import com.hei.course.entity.JUser;
import com.hei.course.mapper.ReservationMapper;
import com.hei.course.model.Reservation;
import com.hei.course.repository.ProjectionRepository;
import com.hei.course.repository.ReservationRepository;
import com.hei.course.repository.UserRepository;
import com.hei.course.security.UserPrincipal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ReservationService {
  private final ReservationRepository reservationRepository;
  private final UserRepository userRepository;
  private final ProjectionRepository projectionRepository;

  public List<Reservation> getAllReservations() {
    return reservationRepository.findAll().stream().map(ReservationMapper::toModel).toList();
  }

  public Reservation getReservationById(UUID id, UserPrincipal currentUser) {
    JReservation entity =
        reservationRepository
            .findById(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

    boolean isClient = "CLIENT".equals(currentUser.getUser().getUserRole().name());
    if (isClient && !entity.getUser().getId().equals(currentUser.getUser().getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot access this reservation");
    }
    return ReservationMapper.toModel(entity);
  }

  public Reservation saveReservation(ReservationInput input) {
    JUser user =
        userRepository
            .findById(input.getUserId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

    JProjection projection =
        projectionRepository
            .findById(input.getProjectionId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Projection not found"));

    JReservation entity;
    if (input.getId() != null) {
      entity =
          reservationRepository
              .findById(input.getId())
              .orElseThrow(
                  () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    } else {
      entity = new JReservation();
      entity.setCreatedAt(Instant.now());
    }

    entity.setUser(user);
    entity.setProjection(projection);

    return ReservationMapper.toModel(reservationRepository.save(entity));
  }
}
