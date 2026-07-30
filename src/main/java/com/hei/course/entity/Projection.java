package com.hei.course.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Projection {
    private UUID id;
    private Instant datetime;
    private BigDecimal seatPrice;
}
