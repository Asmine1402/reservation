package com.hei.course.endpoint.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;
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
public class ProjectionInput {
  private UUID id;
  private Instant datetime;
  private BigDecimal seatPrice;
  private UUID movieId;
  private UUID roomId;
}
