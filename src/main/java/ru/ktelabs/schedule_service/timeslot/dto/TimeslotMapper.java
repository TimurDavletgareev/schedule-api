package ru.ktelabs.schedule_service.timeslot.dto;

import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.util.CustomFormatter;

import java.util.List;
import java.util.stream.StreamSupport;

public class TimeslotMapper {

    public static TimeslotFullDto modelToFullDto(Timeslot timeslot) {

        TimeslotFullDto timeslotFullDto =
                TimeslotFullDto.builder()
                        .id(timeslot.getId())
                        .doctorId(timeslot.getDoctor().getId())
                        .date(CustomFormatter.dateToString(timeslot.getDate()))
                        .startTime(CustomFormatter.timeToString(timeslot.getStartTime()))
                        .duration(timeslot.getDuration())
                        .build();

        if (timeslot.getPatient() != null) {
            timeslotFullDto.setPatientId(timeslotFullDto.getPatientId());
        }

        return timeslotFullDto;
    }

    public static List<TimeslotFullDto> modelToFullDto(Iterable<Timeslot> timeslots) {

        return StreamSupport.stream(timeslots.spliterator(), false)
                .map(TimeslotMapper::modelToFullDto)
                .toList();
    }
}
