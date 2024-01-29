package ru.ktelabs.schedule_service.patient.dto;

import ru.ktelabs.schedule_service.patient.model.Patient;

import java.util.List;
import java.util.stream.StreamSupport;

public class PatientMapper {

    public static PatientFullDto modelToFullDto(Patient patient) {

        return PatientFullDto.builder()
                .id(patient.getId())
                .uuid(patient.getUuid())
                .firstName(patient.getFirstName())
                .secondName(patient.getSecondName())
                .lastName(patient.getLastName())
                .birthDate(patient.getBirthDate())
                .build();
    }

    public static List<PatientFullDto> modelToFullDto(Iterable<Patient> patients) {

        return StreamSupport.stream(patients.spliterator(), false)
                .map(PatientMapper::modelToFullDto)
                .toList();
    }
}
