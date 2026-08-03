package com.hei.course.endpoint.rest.controller;

import com.hei.course.entity.JMovie;
import com.hei.course.mapper.MovieMapper;
import com.hei.course.model.Movie;
import com.hei.course.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {
  private final MovieRepository movieRepository;

  @PutMapping("/movies")
  public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) {
    JMovie entity = MovieMapper.toEntity(movie);
    JMovie saved = movieRepository.save(entity);
    return ResponseEntity.ok(MovieMapper.toModel(saved));
  }
}
