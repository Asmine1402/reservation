package com.hei.course.model;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Movie {
  private UUID id;
  private String title;
  private List<Genre> genre;
  private String description;
  private Duration duration;
  private List<Projection> projectionList;
}
