package com.hei.course.endpoint.rest.dto;

import com.hei.course.model.UserRole;
import java.time.LocalDateTime;

public record UserInput(
        String firstname,
        String lastname,
        LocalDateTime birthdate,
        String email,
        String phone,
        String password,
        UserRole userRole
) {}