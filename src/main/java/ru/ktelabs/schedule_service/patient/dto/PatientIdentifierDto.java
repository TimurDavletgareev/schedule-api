package ru.ktelabs.schedule_service.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Набор идентификаторов пациента, должен присутствовать хотя бы один идентификатор")
public class PatientIdentifierDto {

    @Schema(description = "Идентификатор пациента", example = "123")
    private final Long id;

    @Schema(description = "Идентификатор UUID пациента")
    private final UUID uuid;
}
