package ru.ktelabs.schedule_service.timeslot.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.patient.model.Patient;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timeslots")
@Getter
@Setter
@ToString
public class Timeslot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "duration")
    private int duration;
}
