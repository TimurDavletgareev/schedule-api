package ru.ktelabs.schedule_service.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Обновление доктора")
public class DoctorUpdateDto {

    @Size(min = 2, max = 50, message = "size must be between 2 and 50")
    @Schema(description = "Имя", example = "Иван")
    private final String firstName;

    @Size(min = 2, max = 50, message = "size must be between 2 and 50")
    @Schema(description = "Отчество", example = "Иванович")
    private final String secondName;

    @Size(min = 2, max = 50, message = "size must be between 2 and 50")
    @Schema(description = "Фамилия", example = "Иванов")
    private final String lastName;
}
