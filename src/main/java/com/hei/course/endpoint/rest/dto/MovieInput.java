package com.hei.course.endpoint.rest.dto;

import com.hei.course.model.Genre;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MovieInput {
  private UUID id;
  private String title;
  private List<Genre> genre;
  private String description;
  private Duration duration;
}
