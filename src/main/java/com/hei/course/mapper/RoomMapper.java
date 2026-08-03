package com.hei.course.mapper;

import com.hei.course.entity.JRoom;
import com.hei.course.model.Room;

public class RoomMapper {

  private RoomMapper() {}

  public static Room toModel(JRoom entity) {
    if (entity == null) {
      return null;
    }

    Room room = new Room();
    room.setId(entity.getId());
    room.setNumber(entity.getNumber());
    room.setCapacity(entity.getCapacity());

    return room;
  }

  public static JRoom toEntity(Room model) {
    if (model == null) {
      return null;
    }

    return JRoom.builder()
        .id(model.getId())
        .number(model.getNumber())
        .capacity(model.getCapacity())
        .build();
  }
}
