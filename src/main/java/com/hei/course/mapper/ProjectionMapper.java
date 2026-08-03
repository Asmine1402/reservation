package com.hei.course.mapper;

import com.hei.course.entity.JProjection;
import com.hei.course.model.Projection;

public class ProjectionMapper {

  private ProjectionMapper() {}

  public static Projection toModel(JProjection entity) {
    if (entity == null) {
      return null;
    }

    Projection projection = new Projection();
    projection.setId(entity.getId());
    projection.setDatetime(entity.getDatetime());
    projection.setSeatPrice(entity.getSeatPrice());
    projection.setMovie(MovieMapper.toModel(entity.getMovie()));
    projection.setRoom(RoomMapper.toModel(entity.getRoom()));

    return projection;
  }

  public static JProjection toEntity(Projection model) {
    if (model == null) {
      return null;
    }

    return JProjection.builder()
        .id(model.getId())
        .datetime(model.getDatetime())
        .seatPrice(model.getSeatPrice())
        .movie(MovieMapper.toEntity(model.getMovie()))
        .room(RoomMapper.toEntity(model.getRoom()))
        .build();
  }
}
