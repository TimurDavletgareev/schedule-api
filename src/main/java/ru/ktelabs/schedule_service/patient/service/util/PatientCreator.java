package ru.ktelabs.schedule_service.patient.service.util;

import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.model.Patient;
import ru.ktelabs.schedule_service.util.CustomFormatter;
import ru.ktelabs.schedule_service.util.NullChecker;

import java.util.UUID;

public class PatientCreator {

    public static Patient createFromNewPatientDto(NewPatientDto newPatientDto) {

        Patient patient = new Patient();

        patient.setUuid(UUID.randomUUID());
        patient.setFirstName(newPatientDto.getFirstName());
        NullChecker.setIfNotNull(patient::setSecondName, newPatientDto.getSecondName());
        patient.setLastName(newPatientDto.getLastName());
        patient.setBirthDate(CustomFormatter.stringToDate(newPatientDto.getBirthDate()));

        return patient;
    }
}
