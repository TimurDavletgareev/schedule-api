package ru.ktelabs.schedule_service.timeslot.service;

import ru.ktelabs.schedule_service.patient.dto.PatientIdentifierDto;
import ru.ktelabs.schedule_service.timeslot.dto.NewScheduleDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotFullDto;

import java.util.List;

public interface TimeslotService {

    void addTimeslots(NewScheduleDto newScheduleDto);

    List<TimeslotFullDto> getEmptySlotsByDoctorIdAndDate(Long doctorId, String date);

    TimeslotFullDto reserveById(Long timeslotId, Long patientId);

    TimeslotFullDto cleanById(Long timeslotId);

    List<TimeslotFullDto> getSlotsByPatientIdOrUuid(PatientIdentifierDto patientIdentifierDto);

    void removeById(Long timeslotId);
}
