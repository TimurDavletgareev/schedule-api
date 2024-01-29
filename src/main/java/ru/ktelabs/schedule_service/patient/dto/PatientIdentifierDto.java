package ru.ktelabs.schedule_service.patient.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PatientIdentifierDto {

    private final Long id;
    private final UUID uuid;
}
