package ru.ktelabs.schedule_service.patient.service.util;

import ru.ktelabs.schedule_service.patient.dto.PatientUpdateDto;
import ru.ktelabs.schedule_service.patient.model.Patient;
import ru.ktelabs.schedule_service.util.CustomFormatter;
import ru.ktelabs.schedule_service.util.NullChecker;

public class PatientUpdater {

    public static Patient update(Patient patientToUpdate, PatientUpdateDto patientUpdateDto) {

        NullChecker.setIfNotNull(patientToUpdate::setFirstName, patientUpdateDto.getFirstName());
        NullChecker.setIfNotNull(patientToUpdate::setSecondName, patientUpdateDto.getSecondName());
        NullChecker.setIfNotNull(patientToUpdate::setLastName, patientUpdateDto.getLastName());
        NullChecker.setIfNotNull(patientToUpdate::setBirthDate,
                CustomFormatter.stringToDate(patientUpdateDto.getLastName()));

        return patientToUpdate;
    }
}
