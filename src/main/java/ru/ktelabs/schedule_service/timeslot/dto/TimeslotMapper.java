package ru.ktelabs.schedule_service.timeslot.dto;

import ru.ktelabs.schedule_service.doctor.dto.DoctorMapper;
import ru.ktelabs.schedule_service.patient.dto.PatientMapper;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;

import java.util.List;
import java.util.stream.StreamSupport;

public class TimeslotMapper {

    public static TimeslotFullDto modelToFullDto(Timeslot timeslot) {

        return TimeslotFullDto.builder()
                .id(timeslot.getId())
                .doctorFullDto(DoctorMapper.modelToFullDto(timeslot.getDoctor()))
                .patientFullDto(PatientMapper.modelToFullDto(timeslot.getPatient()))
                .date(timeslot.getDate())
                .startTime(timeslot.getStartTime())
                .duration(timeslot.getDuration())
                .build();
    }

    public static List<TimeslotFullDto> modelToFullDto(Iterable<Timeslot> timeslots) {

        return StreamSupport.stream(timeslots.spliterator(), false)
                .map(TimeslotMapper::modelToFullDto)
                .toList();
    }
}
