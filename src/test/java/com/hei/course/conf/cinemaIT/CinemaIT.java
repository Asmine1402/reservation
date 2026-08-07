package com.hei.course.conf.cinemaIT;

import static org.junit.jupiter.api.Assertions.*;

import com.hei.course.conf.FacadeIT;
import com.hei.course.endpoint.rest.dto.ReservationInput;
import com.hei.course.entity.*;
import com.hei.course.mapper.*;
import com.hei.course.model.*;
import com.hei.course.repository.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CinemaIT extends FacadeIT {

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private SeatRepository seatRepository;
  @Autowired private ProjectionRepository projectionRepository;
  @Autowired private ReservationRepository reservationRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  private static final String CLIENT_EMAIL = "asminerazafiarivelo@gmail.com";
  private static final String CLIENT_PASSWORD = "asmine123";
  private static final String MANAGER_EMAIL = "manoa.manager@gmail.com";
  private static final String MANAGER_PASSWORD = "manager123";
  private JUser userEntity;
  private JProjection projectionEntity;

  @BeforeEach
  void setup() {
    reservationRepository.deleteAll();
    projectionRepository.deleteAll();
    seatRepository.deleteAll();
    roomRepository.deleteAll();
    movieRepository.deleteAll();
    userRepository.deleteAll();
    User userModel =
        new User(
            UUID.randomUUID(),
            "Asmine",
            "Razafy",
            LocalDateTime.parse("2003-02-14T14:30:00"),
            CLIENT_EMAIL,
            "0123456",
            passwordEncoder.encode(CLIENT_PASSWORD),
            UserRole.CLIENT,
            new ArrayList<>());
    userEntity = userRepository.save(UserMapper.toEntity(userModel));
    User managerModel =
        new User(
            UUID.randomUUID(),
            "Manoa",
            "Andriamampianina",
            LocalDateTime.parse("1990-01-01T08:00:00"),
            MANAGER_EMAIL,
            "0345678",
            passwordEncoder.encode(MANAGER_PASSWORD),
            UserRole.MANAGER,
            new ArrayList<>());
    userRepository.save(UserMapper.toEntity(managerModel));

    List<Genre> genre = List.of(Genre.ROMANCE, Genre.DRAMA);
    Movie movieModel =
        new Movie(
            UUID.randomUUID(),
            "PURPLE HEART",
            genre,
            "Romantic movie with Sofia Carson",
            Duration.parse("PT2H30M"),
            new ArrayList<>());
    JMovie movieEntity = movieRepository.save(MovieMapper.toEntity(movieModel));

    Room roomModel = new Room(UUID.randomUUID(), "101A", 50, new ArrayList<>(), new ArrayList<>());
    JRoom roomEntity = roomRepository.save(RoomMapper.toEntity(roomModel));

    Seat seatModel = new Seat(UUID.randomUUID(), "586", roomModel);
    JSeat seatEntity = SeatMapper.toEntity(seatModel);
    seatEntity.setRoom(roomEntity);
    seatRepository.save(seatEntity);

    Projection projectionModel =
        new Projection(
            UUID.randomUUID(),
            Instant.parse("2026-08-14T09:30:00Z"),
            new BigDecimal("40"),
            movieModel,
            roomModel);
    JProjection projectionToSave = ProjectionMapper.toEntity(projectionModel);
    projectionToSave.setMovie(movieEntity);
    projectionToSave.setRoom(roomEntity);
    projectionEntity = projectionRepository.save(projectionToSave);

    Reservation resaModel =
        new Reservation(
            UUID.randomUUID(), Instant.parse("2026-08-06T09:30:00Z"), projectionModel, userModel);
    JReservation resaEntity = ReservationMapper.toEntity(resaModel);
    resaEntity.setUser(userEntity);
    resaEntity.setProjection(projectionEntity);
    reservationRepository.save(resaEntity);
  }

  @Test
  void should_return_403_when_client_tries_to_list_reservations() {
    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange("/reservation", HttpMethod.GET, null, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_reservation_list_for_manager() {
    ResponseEntity<List<Reservation>> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange(
                "/reservation",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Reservation>>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());

    Reservation resa = response.getBody().get(0);
    assertEquals("Asmine", resa.getUser().getFirstname());
    assertEquals("PURPLE HEART", resa.getProjection().getMovie().getTitle());
  }

  @Test
  void should_return_projection_list() {
    ResponseEntity<List<Projection>> response =
        testRestTemplate.exchange(
            "/projection",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Projection>>() {});

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());

    Projection projection = response.getBody().get(0);
    assertEquals(0, new BigDecimal("40").compareTo(projection.getSeatPrice()));
  }

  @Test
  void should_return_401_when_not_authenticated() {
    ResponseEntity<String> response =
        testRestTemplate.exchange(
            "/reservation/" + UUID.randomUUID(), HttpMethod.GET, null, String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void should_return_404_when_reservation_id_unknown() {
    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange("/reservation/" + UUID.randomUUID(), HttpMethod.GET, null, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void should_return_403_when_client_access_another_user_reservation() {
    User otherUserModel =
        new User(
            UUID.randomUUID(),
            "Fanja",
            "Rakoto",
            LocalDateTime.parse("1998-05-20T10:00:00"),
            "fanja@gmail.com",
            "0987654",
            passwordEncoder.encode("fanja123"),
            UserRole.CLIENT,
            new ArrayList<>());
    JUser otherUserEntity = userRepository.save(UserMapper.toEntity(otherUserModel));

    JReservation otherResaEntity = new JReservation();
    otherResaEntity.setId(UUID.randomUUID());
    otherResaEntity.setCreatedAt(Instant.now());
    otherResaEntity.setUser(otherUserEntity);
    otherResaEntity.setProjection(projectionEntity);
    otherResaEntity = reservationRepository.save(otherResaEntity);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange(
                "/reservation/" + otherResaEntity.getId(), HttpMethod.GET, null, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_403_when_client_tries_to_put_reservation() {
    ReservationInput input = new ReservationInput();
    input.setUserId(userEntity.getId());
    input.setProjectionId(projectionEntity.getId());

    HttpEntity<ReservationInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange("/reservation", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_400_when_user_not_found_on_save() {
    ReservationInput input = new ReservationInput();
    input.setUserId(UUID.randomUUID());
    input.setProjectionId(projectionEntity.getId());

    HttpEntity<ReservationInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/reservation", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void should_return_400_when_projection_not_found_on_save() {
    ReservationInput input = new ReservationInput();
    input.setUserId(userEntity.getId());
    input.setProjectionId(UUID.randomUUID());

    HttpEntity<ReservationInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/reservation", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void should_save_reservation_when_manager_puts_valid_input() {
    ReservationInput input = new ReservationInput();
    input.setUserId(userEntity.getId());
    input.setProjectionId(projectionEntity.getId());

    HttpEntity<ReservationInput> request = new HttpEntity<>(input);

    ResponseEntity<Reservation> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/reservation", HttpMethod.PUT, request, Reservation.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(userEntity.getId(), response.getBody().getUser().getId());
    assertEquals(projectionEntity.getId(), response.getBody().getProjection().getId());
  }

  @Test
  void should_return_reservation_by_id_when_owner() {
    // Récupère directement l'id de l'entité JPA (pas de mapping -> pas de lazy loading)
    UUID reservationId = reservationRepository.findAll().stream().findFirst().orElseThrow().getId();

    ResponseEntity<Reservation> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange("/reservation/" + reservationId, HttpMethod.GET, null, Reservation.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Asmine", response.getBody().getUser().getFirstname());
  }

  @Test
  void should_return_null_when_entity_or_model_is_null() {
    assertNull(ReservationMapper.toModel(null));
    assertNull(ReservationMapper.toEntity(null));
  }
}
