package com.hei.course.mapper;

import com.hei.course.entity.JMovie;
import com.hei.course.model.Movie;
import java.util.ArrayList;

public class MovieMapper {

  private MovieMapper() {}

  public static Movie toModel(JMovie entity) {
    if (entity == null) {
      return null;
    }

    Movie movie = new Movie();
    movie.setId(entity.getId());
    movie.setTitle(entity.getTitle());
    movie.setGenre(new ArrayList<>(entity.getGenre()));
    movie.setDescription(entity.getDescription());
    movie.setDuration(entity.getDuration());

    return movie;
  }

  public static JMovie toEntity(Movie model) {
    if (model == null) {
      return null;
    }

    return JMovie.builder()
        .id(model.getId())
        .title(model.getTitle())
        .genre(model.getGenre() == null ? new ArrayList<>() : new ArrayList<>(model.getGenre()))
        .description(model.getDescription())
        .duration(model.getDuration())
        .build();
  }
}
