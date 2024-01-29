package ru.ktelabs.schedule_service.doctor.service.util;

import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.doctor.repository.DoctorRepository;
import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;

public class DoctorChecker {

    public static void check(DoctorRepository doctorRepository, Doctor doctorToCheck) {

        if (doctorRepository.existsByFirstNameAndSecondNameAndLastName(
                doctorToCheck.getFirstName(),
                doctorToCheck.getSecondName(),
                doctorToCheck.getLastName()
        )) {
            throw new ConflictOnRequestException("- Doctor with such full name already exists");
        }
    }
}
