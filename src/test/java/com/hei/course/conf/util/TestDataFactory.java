package com.hei.course.conf.util;

import com.hei.course.model.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestDataFactory {

  public static User createTestUser() {
    return User.builder()
        .id(UUID.randomUUID())
        .firstname("Asmine")
        .lastname("RAZAFY")
        .email("asminerazafiarivelo@gmail.com")
        .phone("0312466")
        .birthdate(LocalDateTime.parse("2003-02-14T00:00:00"))
        .userRole(UserRole.CLIENT)
        .build();
  }

  public static Movie createTestMovie() {
    return Movie.builder()
        .id(UUID.randomUUID())
        .title("Inception")
        .description("Film de science-fiction")
        .duration(Duration.ofMinutes(148))
        .genre(List.of(Genre.SCIENCE_FICTION, Genre.ACTION))
        .build();
  }

  public static Room createTestRoom() {
    Room room = Room.builder().id(UUID.randomUUID()).number("Room-1").capacity(100).build();

    Seat seat1 = Seat.builder().id(UUID.randomUUID()).number("A1").room(room).build();

    Seat seat2 = Seat.builder().id(UUID.randomUUID()).number("A2").room(room).build();

    room.setSeatList(List.of(seat1, seat2));
    return room;
  }

  public static Projection createTestProjection(Movie movie, Room room) {
    return Projection.builder()
        .id(UUID.randomUUID())
        .datetime(Instant.parse("2026-08-05T20:00:00Z"))
        .seatPrice(BigDecimal.valueOf(10.0))
        .movie(movie)
        .room(room)
        .build();
  }
}
