package ru.ktelabs.schedule_service.timeslot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import ru.ktelabs.schedule_service.util.Constants;

@Data
@Builder
@Schema(description = "Слот времени")
public class TimeslotFullDto {

    @Schema(description = "Идентификатор", example = "123")
    private final Long id;

    @Schema(description = "Идентификатор врача", example = "123")
    private final Long doctorId;

    @Schema(description = "Идентификатор пациента", example = "123")
    @Setter
    private Long patientId;

    @Schema(description = "Дата", example = Constants.DATE_FORMAT)
    private final String date;

    @Schema(description = "Время начала", example = Constants.TIME_FORMAT)
    private final String startTime;

    @Schema(description = "Длительность в минутах", example = "30")
    private final int duration;
}
