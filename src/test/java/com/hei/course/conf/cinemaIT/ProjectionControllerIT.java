package com.hei.course.conf.cinemaIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hei.course.conf.FacadeIT;
import com.hei.course.endpoint.rest.dto.ProjectionInput;
import com.hei.course.entity.JMovie;
import com.hei.course.entity.JRoom;
import com.hei.course.mapper.MovieMapper;
import com.hei.course.mapper.RoomMapper;
import com.hei.course.model.Genre;
import com.hei.course.model.Movie;
import com.hei.course.model.Projection;
import com.hei.course.model.Room;
import com.hei.course.model.User;
import com.hei.course.model.UserRole;
import com.hei.course.repository.MovieRepository;
import com.hei.course.repository.ProjectionRepository;
import com.hei.course.repository.ReservationRepository;
import com.hei.course.repository.RoomRepository;
import com.hei.course.repository.SeatRepository;
import com.hei.course.repository.UserRepository;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProjectionControllerIT extends FacadeIT {

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private SeatRepository seatRepository;
  @Autowired private ProjectionRepository projectionRepository;
  @Autowired private ReservationRepository reservationRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  private static final String CLIENT_EMAIL = "client.projection@gmail.com";
  private static final String CLIENT_PASSWORD = "client123";
  private static final String EMPLOYEE_EMAIL = "employee.projection@gmail.com";
  private static final String EMPLOYEE_PASSWORD = "employee123";
  private static final String MANAGER_EMAIL = "manager.projection@gmail.com";
  private static final String MANAGER_PASSWORD = "manager123";

  private JMovie movieEntity;
  private JRoom roomEntity;

  @BeforeEach
  void setup() {
    // La base de test est partagée entre toutes les classes IT du même worker Gradle
    // (voir FacadeIT) : il faut nettoyer dans l'ordre des FK, pas seulement nos propres tables.
    reservationRepository.deleteAll();
    projectionRepository.deleteAll();
    seatRepository.deleteAll();
    roomRepository.deleteAll();
    movieRepository.deleteAll();
    userRepository.deleteAll();

    createUser("Client", CLIENT_EMAIL, CLIENT_PASSWORD, UserRole.CLIENT);
    createUser("Employee", EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD, UserRole.EMPLOYEE);
    createUser("Manager", MANAGER_EMAIL, MANAGER_PASSWORD, UserRole.MANAGER);

    Movie movieModel =
        Movie.builder()
            .id(UUID.randomUUID())
            .title("PURPLE HEART")
            .genre(List.of(Genre.ROMANCE, Genre.DRAMA))
            .description("Romantic movie")
            .duration(Duration.parse("PT2H30M"))
            .build();
    movieEntity = movieRepository.save(MovieMapper.toEntity(movieModel));

    Room roomModel = Room.builder().id(UUID.randomUUID()).number("101A").capacity(50).build();
    roomEntity = roomRepository.save(RoomMapper.toEntity(roomModel));
  }

  private void createUser(String firstname, String email, String password, UserRole role) {
    User userModel =
        new User(
            UUID.randomUUID(),
            firstname,
            "Test",
            LocalDateTime.parse("1998-05-20T10:00:00"),
            email,
            "0300000000",
            passwordEncoder.encode(password),
            role,
            new ArrayList<>());
    userRepository.save(com.hei.course.mapper.UserMapper.toEntity(userModel));
  }

  private ProjectionInput buildProjectionInput() {
    ProjectionInput input = new ProjectionInput();
    input.setDatetime(Instant.parse("2026-09-01T20:00:00Z"));
    input.setSeatPrice(new BigDecimal("35"));
    input.setMovieId(movieEntity.getId());
    input.setRoomId(roomEntity.getId());
    return input;
  }

  @Test
  void should_return_403_when_client_tries_to_save_projection() {
    HttpEntity<ProjectionInput> request = new HttpEntity<>(buildProjectionInput());

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange("/projection", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_403_when_employee_tries_to_save_projection() {
    HttpEntity<ProjectionInput> request = new HttpEntity<>(buildProjectionInput());

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD)
            .exchange("/projection", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_401_when_not_authenticated() {
    HttpEntity<ProjectionInput> request = new HttpEntity<>(buildProjectionInput());

    ResponseEntity<String> response =
        testRestTemplate.exchange("/projection", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void should_create_projection_when_manager_puts_valid_input() {
    HttpEntity<ProjectionInput> request = new HttpEntity<>(buildProjectionInput());

    ResponseEntity<Projection> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/projection", HttpMethod.PUT, request, Projection.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, new BigDecimal("35").compareTo(response.getBody().getSeatPrice()));
    assertEquals(movieEntity.getId(), response.getBody().getMovie().getId());
    assertEquals(1, projectionRepository.findAll().size());
  }

  @Test
  void should_return_400_when_movie_not_found_on_save() {
    ProjectionInput input = buildProjectionInput();
    input.setMovieId(UUID.randomUUID());
    HttpEntity<ProjectionInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/projection", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void should_return_400_when_room_not_found_on_save() {
    ProjectionInput input = buildProjectionInput();
    input.setRoomId(UUID.randomUUID());
    HttpEntity<ProjectionInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/projection", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void should_return_404_when_manager_updates_unknown_projection() {
    ProjectionInput input = buildProjectionInput();
    input.setId(UUID.randomUUID());
    HttpEntity<ProjectionInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/projection", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
