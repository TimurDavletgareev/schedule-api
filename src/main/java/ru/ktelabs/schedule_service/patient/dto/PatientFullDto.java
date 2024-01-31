package ru.ktelabs.schedule_service.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.ktelabs.schedule_service.util.Constants;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Пациент")
public class PatientFullDto {

    @Schema(description = "Идентификатор", example = "123")
    private final Long id;

    @Schema(description = "Идентификатор UUID")
    private final UUID uuid;

    @Schema(description = "Имя", example = "Иван")
    private final String firstName;

    @Schema(description = "Отчество", example = "Иванович")
    private final String secondName;

    @Schema(description = "Фамилия", example = "Иванов")
    private final String lastName;

    @Schema(description = "Дата рождения", example = Constants.DATE_FORMAT)
    private final String birthDate;
}
