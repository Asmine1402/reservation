package com.hei.course.mapper;

import com.hei.course.entity.JSeat;
import com.hei.course.model.Seat;

public class SeatMapper {

  private SeatMapper() {}

  public static Seat toModel(JSeat entity) {
    if (entity == null) {
      return null;
    }

    Seat seat = new Seat();
    seat.setId(entity.getId());
    seat.setNumber(entity.getNumber());
    seat.setRoom(RoomMapper.toModel(entity.getRoom()));

    return seat;
  }

  public static JSeat toEntity(Seat model) {
    if (model == null) {
      return null;
    }

    return JSeat.builder()
        .id(model.getId())
        .number(model.getNumber())
        .room(RoomMapper.toEntity(model.getRoom()))
        .build();
  }
}
