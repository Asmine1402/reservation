package com.hei.course.conf.cinemaIT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.hei.course.conf.FacadeIT;
import com.hei.course.endpoint.rest.dto.MovieInput;
import com.hei.course.entity.JMovie;
import com.hei.course.mapper.MovieMapper;
import com.hei.course.model.Genre;
import com.hei.course.model.Movie;
import com.hei.course.model.User;
import com.hei.course.model.UserRole;
import com.hei.course.repository.MovieRepository;
import com.hei.course.repository.ProjectionRepository;
import com.hei.course.repository.ReservationRepository;
import com.hei.course.repository.RoomRepository;
import com.hei.course.repository.SeatRepository;
import com.hei.course.repository.UserRepository;
import java.time.Duration;
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

public class MovieControllerIT extends FacadeIT {

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private UserRepository userRepository;
  @Autowired private MovieRepository movieRepository;
  @Autowired private RoomRepository roomRepository;
  @Autowired private SeatRepository seatRepository;
  @Autowired private ProjectionRepository projectionRepository;
  @Autowired private ReservationRepository reservationRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  private static final String CLIENT_EMAIL = "client.movie@gmail.com";
  private static final String CLIENT_PASSWORD = "client123";
  private static final String EMPLOYEE_EMAIL = "employee.movie@gmail.com";
  private static final String EMPLOYEE_PASSWORD = "employee123";
  private static final String MANAGER_EMAIL = "manager.movie@gmail.com";
  private static final String MANAGER_PASSWORD = "manager123";

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

  private MovieInput buildMovieInput() {
    MovieInput input = new MovieInput();
    input.setTitle("Inception");
    input.setGenre(List.of(Genre.SCIENCE_FICTION, Genre.ACTION));
    input.setDescription("Un voleur qui s'infiltre dans les rêves");
    input.setDuration(Duration.ofMinutes(148));
    return input;
  }

  @Test
  void should_return_403_when_client_tries_to_save_movie() {
    HttpEntity<MovieInput> request = new HttpEntity<>(buildMovieInput());

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(CLIENT_EMAIL, CLIENT_PASSWORD)
            .exchange("/movies", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_403_when_employee_tries_to_save_movie() {
    HttpEntity<MovieInput> request = new HttpEntity<>(buildMovieInput());

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(EMPLOYEE_EMAIL, EMPLOYEE_PASSWORD)
            .exchange("/movies", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  void should_return_401_when_not_authenticated() {
    HttpEntity<MovieInput> request = new HttpEntity<>(buildMovieInput());

    ResponseEntity<String> response =
        testRestTemplate.exchange("/movies", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  void should_create_movie_when_manager_puts_valid_input() {
    HttpEntity<MovieInput> request = new HttpEntity<>(buildMovieInput());

    ResponseEntity<Movie> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/movies", HttpMethod.PUT, request, Movie.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Inception", response.getBody().getTitle());
    assertEquals(1, movieRepository.findAll().size());
  }

  @Test
  void should_update_existing_movie_when_manager_puts_with_id() {
    Movie existing =
        Movie.builder()
            .id(UUID.randomUUID())
            .title("Old title")
            .genre(List.of(Genre.DRAMA))
            .description("Old description")
            .duration(Duration.ofMinutes(100))
            .build();
    JMovie saved = movieRepository.save(MovieMapper.toEntity(existing));

    MovieInput input = buildMovieInput();
    input.setId(saved.getId());
    input.setTitle("Updated title");
    HttpEntity<MovieInput> request = new HttpEntity<>(input);

    ResponseEntity<Movie> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/movies", HttpMethod.PUT, request, Movie.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(saved.getId(), response.getBody().getId());
    assertEquals("Updated title", response.getBody().getTitle());
    assertEquals(1, movieRepository.findAll().size());
  }

  @Test
  void should_return_404_when_manager_updates_unknown_movie() {
    MovieInput input = buildMovieInput();
    input.setId(UUID.randomUUID());
    HttpEntity<MovieInput> request = new HttpEntity<>(input);

    ResponseEntity<String> response =
        testRestTemplate
            .withBasicAuth(MANAGER_EMAIL, MANAGER_PASSWORD)
            .exchange("/movies", HttpMethod.PUT, request, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
