package ru.ktelabs.schedule_service.timeslot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.ktelabs.schedule_service.util.Constants;

@Data
@Builder
public class NewScheduleDto {

    @NotNull
    private final Long doctorId;

    @NotNull
    @Size(min = Constants.DATE_SIZE, max = Constants.DATE_SIZE, message = "Date size must be " + Constants.DATE_SIZE)
    private final String date;

    @NotNull
    @Size(min = Constants.TIME_SIZE, max = Constants.TIME_SIZE, message = "Date size must be " + Constants.TIME_SIZE)
    private final String startTime;

    @NotNull
    private final int duration;

    @NotNull
    private final int amount;
}
