package com.hei.course.service;

import com.hei.course.endpoint.rest.dto.MovieInput;
import com.hei.course.entity.JMovie;
import com.hei.course.mapper.MovieMapper;
import com.hei.course.model.Movie;
import com.hei.course.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class MovieService {

  private final MovieRepository movieRepository;

  public Movie saveMovie(MovieInput input) {
    JMovie entity;

    if (input.getId() != null) {
      entity =
          movieRepository
              .findById(input.getId())
              .orElseThrow(
                  () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    } else {
      entity = new JMovie();
    }

    entity.setTitle(input.getTitle());
    entity.setGenre(input.getGenre());
    entity.setDescription(input.getDescription());
    entity.setDuration(input.getDuration());

    return MovieMapper.toModel(movieRepository.save(entity));
  }
}
