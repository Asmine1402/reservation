package com.hei.course.endpoint.rest.controller;

import com.hei.course.endpoint.rest.dto.ReservationInput;
import com.hei.course.model.Reservation;
import com.hei.course.security.UserPrincipal;
import com.hei.course.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@RestController
public class ReservationController {
    private final ReservationService service;
    @GetMapping()
    public List<Reservation> getReservations() {
        return service.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(
            @PathVariable UUID id, @AuthenticationPrincipal UserPrincipal currentUser) {
        return service.getReservationById(id, currentUser);
    }

    @PutMapping()
    public Reservation saveReservation(@RequestBody ReservationInput input) {
        return service.saveReservation(input);
    }

}
