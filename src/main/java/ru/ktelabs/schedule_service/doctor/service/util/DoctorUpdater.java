package ru.ktelabs.schedule_service.doctor.service.util;

import ru.ktelabs.schedule_service.doctor.dto.DoctorUpdateDto;
import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.util.NullChecker;

public class DoctorUpdater {

    public static Doctor update(Doctor doctorToUpdate, DoctorUpdateDto doctorUpdateDto) {

        NullChecker.setIfNotNull(doctorToUpdate::setFirstName, doctorUpdateDto.getFirstName());
        NullChecker.setIfNotNull(doctorToUpdate::setSecondName, doctorUpdateDto.getSecondName());
        NullChecker.setIfNotNull(doctorToUpdate::setLastName, doctorUpdateDto.getLastName());

        return doctorToUpdate;
    }
}
