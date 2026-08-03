package com.hei.course.mapper;

import com.hei.course.entity.JReservation;
import com.hei.course.model.Reservation;

public class ReservationMapper {

  private ReservationMapper() {}

  public static Reservation toModel(JReservation entity) {
    if (entity == null) {
      return null;
    }

    Reservation reservation = new Reservation();
    reservation.setId(entity.getId());
    reservation.setCreatedAt(entity.getCreatedAt());
    reservation.setProjection(ProjectionMapper.toModel(entity.getProjection()));
    reservation.setUser(UserMapper.toModel(entity.getUser()));

    return reservation;
  }

  public static JReservation toEntity(Reservation model) {
    if (model == null) {
      return null;
    }

    return JReservation.builder()
        .id(model.getId())
        .createdAt(model.getCreatedAt())
        .projection(ProjectionMapper.toEntity(model.getProjection()))
        .user(UserMapper.toEntity(model.getUser()))
        .build();
  }
}
