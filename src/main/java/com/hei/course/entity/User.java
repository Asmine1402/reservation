package com.hei.course.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private UUID id;
    private String firstname;
    private String lastname;
    private LocalDateTime birthdate;
    private String email;
    private String phone;
    private String password;
    private UserRole userRole;
}
