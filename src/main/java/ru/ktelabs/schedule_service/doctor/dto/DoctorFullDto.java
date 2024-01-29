package ru.ktelabs.schedule_service.doctor.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DoctorFullDto {

    private final Long id;

    private final UUID uuid;

    private final String firstName;

    private final String secondName;

    private final String lastName;
}
