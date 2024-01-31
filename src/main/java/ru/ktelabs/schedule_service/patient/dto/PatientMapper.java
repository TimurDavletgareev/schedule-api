package ru.ktelabs.schedule_service.patient.dto;

import ru.ktelabs.schedule_service.patient.model.Patient;
import ru.ktelabs.schedule_service.util.CustomFormatter;

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
                .birthDate(CustomFormatter.dateToString(patient.getBirthDate()))
                .build();
    }

    public static List<PatientFullDto> modelToFullDto(Iterable<Patient> patients) {

        return StreamSupport.stream(patients.spliterator(), false)
                .map(PatientMapper::modelToFullDto)
                .toList();
    }
}
