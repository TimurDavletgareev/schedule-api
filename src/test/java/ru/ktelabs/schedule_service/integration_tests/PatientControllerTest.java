package ru.ktelabs.schedule_service.integration_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;
import ru.ktelabs.schedule_service.error.exception.NotFoundException;
import ru.ktelabs.schedule_service.patient.controller.PatientController;
import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PatientControllerTest {

    private final PatientController patientController;
    private static NewPatientDto properNewPatientDto;
    private static Long id;

    @BeforeEach
    void setUp() {

        String birthDate = "01.01.1990";
        properNewPatientDto = NewPatientDto.builder()
                .firstName("ProperFirstName")
                .secondName("ProperSecondName")
                .lastName("ProperLastName")
                .birthDate(birthDate)
                .build();
    }

    @Test
    void shouldAddPatient() {

        PatientFullDto patientDtoToCheck = patientController.addPatient(properNewPatientDto);

        assertEquals(properNewPatientDto.getFirstName(), patientDtoToCheck.getFirstName());
        assertEquals(properNewPatientDto.getSecondName(), patientDtoToCheck.getSecondName());
        assertEquals(properNewPatientDto.getLastName(), patientDtoToCheck.getLastName());
        assertEquals(properNewPatientDto.getBirthDate(), patientDtoToCheck.getBirthDate());

        // error with same full name
        assertThrows(ConflictOnRequestException.class, () -> patientController.addPatient(properNewPatientDto));
    }

    @Test
    void shouldGetById() {

        PatientFullDto patientDto = patientController.addPatient(properNewPatientDto);
        id = patientDto.getId();

        PatientFullDto patientDtoToCheck = patientController.getById(id);

        assertEquals(patientDto, patientDtoToCheck);

        // error with wrong name
        assertThrows(NotFoundException.class, () -> patientController.getById(id - 999));
    }

    @Test
    void shouldRemoveById() {

        PatientFullDto patientFullDto = patientController.addPatient(properNewPatientDto);
        id = patientFullDto.getId();

        PatientFullDto doctorDtoToCheck = patientController.getById(id);

        assertEquals(patientFullDto, doctorDtoToCheck);

        // error with wrong id
        assertThrows(NotFoundException.class, () -> patientController.getById(-999L));

        patientController.removeById(id);

        assertThrows(NotFoundException.class, () -> patientController.getById(id));
    }
}