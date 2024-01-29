package ru.ktelabs.schedule_service.timeslot.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewScheduleDto {

    @NotNull
    private final Long doctorId;

    @NotNull
    @Size(min = 8, message = "size must be between 8 and 10")
    @Size(max = 10, message = "size must be between 8 and 10")
    private final String date;

    @NotNull
    @Size(min = 5, message = "size must be between 5 and 8")
    @Size(max = 8, message = "size must be between 5 and 8")
    private final String startTime;

    @NotNull
    private final int duration;

    @NotNull
    private final int amount;
}
