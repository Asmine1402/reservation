package com.hei.course.endpoint.rest.controller;

import com.hei.course.endpoint.rest.dto.ProjectionInput;
import com.hei.course.model.Projection;
import com.hei.course.service.ProjectionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/projection")
@RestController
public class ProjectionController {

  private final ProjectionService service;

  @GetMapping()
  public List<Projection> getProjections(
      @RequestParam(name = "limit", defaultValue = "20") int limit,
      @RequestParam(name = "offset", defaultValue = "0") int offset) {
    return service.getProjections(limit, offset);
  }

  @PutMapping()
  public Projection saveProjection(@RequestBody ProjectionInput input) {
    return service.saveProjection(input);
  }
}
