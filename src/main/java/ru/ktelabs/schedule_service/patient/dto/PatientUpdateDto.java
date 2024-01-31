package ru.ktelabs.schedule_service.patient.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.ktelabs.schedule_service.util.Constants;

@Data
@Builder
@Schema(description = "Обновление пациента")
public class PatientUpdateDto {

    @Size(min = 2, max = 50, message = "size must be between 2 and 50")
    @Schema(description = "Имя", example = "Иван")
    private final String firstName;

    @Size(min = 2, max = 50, message = "size must be between 2 and 50")
    @Schema(description = "Отчество", example = "Иванович")
    private final String secondName;

    @Size(min = 2, max = 50, message = "size must be between 2 and 50")
    @Schema(description = "Фамилия", example = "Иванов")
    private final String lastName;

    @Size(min = Constants.DATE_SIZE, max = Constants.DATE_SIZE, message = "Date size must be " + Constants.DATE_SIZE)
    @Schema(description = "Дата рождения", example = Constants.DATE_FORMAT)
    private final String birthDate;
}
