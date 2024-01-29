package ru.ktelabs.schedule_service.doctor.dto;

import ru.ktelabs.schedule_service.doctor.model.Doctor;

import java.util.List;
import java.util.stream.StreamSupport;

public class DoctorMapper {

    public static DoctorFullDto modelToFullDto(Doctor doctor) {

        return DoctorFullDto.builder()
                .id(doctor.getId())
                .uuid(doctor.getUuid())
                .firstName(doctor.getFirstName())
                .secondName(doctor.getSecondName())
                .lastName(doctor.getLastName())
                .build();
    }

    public static List<DoctorFullDto> modelToFullDto(Iterable<Doctor> doctors) {

        return StreamSupport.stream(doctors.spliterator(), false)
                .map(DoctorMapper::modelToFullDto)
                .toList();
    }
}
