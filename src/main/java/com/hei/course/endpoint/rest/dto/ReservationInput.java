package com.hei.course.endpoint.rest.dto;

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
public class ReservationInput {
  private UUID id;
  private UUID userId;
  private UUID projectionId;
  private List<UUID> seatIds;
}
