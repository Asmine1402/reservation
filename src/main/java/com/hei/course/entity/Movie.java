package com.hei.course.entity;

import lombok.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
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
