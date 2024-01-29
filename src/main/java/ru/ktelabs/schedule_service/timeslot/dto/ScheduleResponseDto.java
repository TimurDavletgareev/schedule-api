package ru.ktelabs.schedule_service.timeslot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleResponseDto {

    private final Long doctorId;

    private final String date;

    private final String startTime;

    private final int duration;

    private final int amount;

}
