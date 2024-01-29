package ru.ktelabs.schedule_service.patient.service;

import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;
import ru.ktelabs.schedule_service.patient.dto.PatientUpdateDto;

public interface PatientService {

    PatientFullDto addPatient(NewPatientDto newPatientDto);

    PatientFullDto updatePatient(Long patientId, PatientUpdateDto patientUpdateDto);

    PatientFullDto getById(Long patientId);

    void removeById(Long patientId);
}
