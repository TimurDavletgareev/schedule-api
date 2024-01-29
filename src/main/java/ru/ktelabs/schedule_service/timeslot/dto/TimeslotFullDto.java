package ru.ktelabs.schedule_service.timeslot.dto;

import lombok.Builder;
import lombok.Data;
import ru.ktelabs.schedule_service.doctor.dto.DoctorFullDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TimeslotFullDto {

    private final Long id;

    private final DoctorFullDto doctorFullDto;

    private final PatientFullDto patientFullDto;

    private final LocalDate date;

    private final LocalTime startTime;

    private final int duration;
}
