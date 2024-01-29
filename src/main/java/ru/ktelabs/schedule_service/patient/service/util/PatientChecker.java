package ru.ktelabs.schedule_service.patient.service.util;

import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;
import ru.ktelabs.schedule_service.patient.model.Patient;
import ru.ktelabs.schedule_service.patient.repository.PatientRepository;

public class PatientChecker {

    public static void checkPatient(PatientRepository patientRepository, Patient patientToCheck) {

        if (patientRepository.existsByFirstNameAndSecondNameAndLastNameAndBirthDate(
                patientToCheck.getFirstName(),
                patientToCheck.getSecondName(),
                patientToCheck.getLastName(),
                patientToCheck.getBirthDate()
        )) {

            throw new ConflictOnRequestException("- Patient with such full name already exists");
        }
    }
}
