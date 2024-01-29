package ru.ktelabs.schedule_service.integration_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.schedule_service.doctor.controller.DoctorController;
import ru.ktelabs.schedule_service.doctor.dto.DoctorFullDto;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;
import ru.ktelabs.schedule_service.error.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DoctorControllerTest {

    private final DoctorController doctorController;
    private static NewDoctorDto properNewDoctorDto;
    private static Long id;

    @BeforeEach
    void setUp() {

        properNewDoctorDto = NewDoctorDto.builder()
                .firstName("ProperFirstName")
                .secondName("ProperSecondName")
                .lastName("ProperLastName")
                .build();
    }

    @Test
    void shouldAddDoctor() {

        DoctorFullDto doctorDtoToCheck = doctorController.addDoctor(properNewDoctorDto);

        assertEquals(properNewDoctorDto.getFirstName(), doctorDtoToCheck.getFirstName());
        assertEquals(properNewDoctorDto.getSecondName(), doctorDtoToCheck.getSecondName());
        assertEquals(properNewDoctorDto.getLastName(), doctorDtoToCheck.getLastName());

        // error with same full name
        assertThrows(ConflictOnRequestException.class, () -> doctorController.addDoctor(properNewDoctorDto));
    }

    @Test
    void shouldGetById() {

        DoctorFullDto doctorDto = doctorController.addDoctor(properNewDoctorDto);
        id = doctorDto.getId();

        DoctorFullDto doctorDtoToCheck = doctorController.getById(id);

        assertEquals(doctorDto, doctorDtoToCheck);

        // error with wrong name
        assertThrows(NotFoundException.class, () -> doctorController.getById(id - 999));
    }

    @Test
    void shouldGetAll() {

        String testName = "name1";
        DoctorFullDto doctorDto1 = doctorController.addDoctor(
                NewDoctorDto.builder()
                        .firstName(testName)
                        .secondName(testName)
                        .lastName(testName)
                        .build());

        testName = "name2";
        DoctorFullDto doctorDto2 = doctorController.addDoctor(
                NewDoctorDto.builder()
                        .firstName(testName)
                        .secondName(testName)
                        .lastName(testName)
                        .build());

        testName = "name3";
        DoctorFullDto doctorDto3 = doctorController.addDoctor(
                NewDoctorDto.builder()
                        .firstName(testName)
                        .secondName(testName)
                        .lastName(testName)
                        .build());

        List<DoctorFullDto> foundDoctors = doctorController.getAll("null", 0, 10);

        assertEquals(3, foundDoctors.size());
        assertEquals("name1", foundDoctors.get(0).getFirstName());
        assertEquals("name2", foundDoctors.get(1).getFirstName());
        assertEquals("name3", foundDoctors.get(2).getFirstName());

    }

    @Test
    void shouldRemoveById() {

        DoctorFullDto doctorDto = doctorController.addDoctor(properNewDoctorDto);
        id = doctorDto.getId();

        DoctorFullDto doctorDtoToCheck = doctorController.getById(id);

        assertEquals(doctorDto, doctorDtoToCheck);

        doctorController.removeById(id);

        assertThrows(NotFoundException.class, () -> doctorController.getById(id));
    }
}