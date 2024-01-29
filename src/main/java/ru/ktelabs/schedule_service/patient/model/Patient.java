package ru.ktelabs.schedule_service.patient.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Getter
@Setter
@ToString
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    private UUID uuid;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

}
