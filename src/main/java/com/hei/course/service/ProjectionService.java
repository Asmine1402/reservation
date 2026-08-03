package com.hei.course.service;

import com.hei.course.endpoint.rest.dto.ProjectionInput;
import com.hei.course.entity.JMovie;
import com.hei.course.entity.JProjection;
import com.hei.course.entity.JRoom;
import com.hei.course.mapper.ProjectionMapper;
import com.hei.course.model.Projection;
import com.hei.course.repository.MovieRepository;
import com.hei.course.repository.ProjectionRepository;
import com.hei.course.repository.RoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ProjectionService {

  private final ProjectionRepository projectionRepository;
  private final MovieRepository movieRepository;
  private final RoomRepository roomRepository;

  public List<Projection> getProjections(int limit, int offset) {
    int pageNumber = offset / limit;
    Pageable pageable = PageRequest.of(pageNumber, limit);

    return projectionRepository.findAll(pageable).stream().map(ProjectionMapper::toModel).toList();
  }

  public Projection saveProjection(ProjectionInput input) {
    JMovie movie =
        movieRepository
            .findById(input.getMovieId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie not found"));

    JRoom room =
        roomRepository
            .findById(input.getRoomId())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room not found"));

    JProjection entity;
    if (input.getId() != null) {
      entity =
          projectionRepository
              .findById(input.getId())
              .orElseThrow(
                  () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projection not found"));
    } else {
      entity = new JProjection();
    }

    entity.setDatetime(input.getDatetime());
    entity.setSeatPrice(input.getSeatPrice());
    entity.setMovie(movie);
    entity.setRoom(room);

    return ProjectionMapper.toModel(projectionRepository.save(entity));
  }
}
