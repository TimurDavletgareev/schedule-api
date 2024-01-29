package ru.ktelabs.schedule_service.doctor.service.util;

import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.util.NullChecker;

import java.util.UUID;

public class DoctorCreator {

    public static Doctor createFromNewDoctorDto(NewDoctorDto newDoctorDto) {

        Doctor doctor = new Doctor();

        doctor.setUuid(UUID.randomUUID());
        doctor.setFirstName(newDoctorDto.getFirstName());
        NullChecker.setIfNotNull(doctor::setSecondName, newDoctorDto.getSecondName());
        doctor.setLastName(newDoctorDto.getLastName());

        return doctor;
    }
}
