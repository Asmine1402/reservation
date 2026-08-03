package com.hei.course.endpoint.rest.controller;

import com.hei.course.endpoint.rest.dto.MovieInput;
import com.hei.course.model.Movie;
import com.hei.course.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MovieController {

  private final MovieService service;

  @PutMapping("/movies")
  public Movie saveMovie(@RequestBody MovieInput input) {
    return service.saveMovie(input);
  }
}
