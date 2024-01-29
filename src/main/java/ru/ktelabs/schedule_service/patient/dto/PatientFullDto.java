package ru.ktelabs.schedule_service.patient.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PatientFullDto {

    private final Long id;

    private final UUID uuid;

    private final String firstName;

    private final String secondName;

    private final String lastName;

    private final LocalDate birthDate;
}
