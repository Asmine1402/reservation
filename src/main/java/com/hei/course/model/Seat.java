package com.hei.course.entity;

import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Seat {
  private UUID id;
  private String number;
}
