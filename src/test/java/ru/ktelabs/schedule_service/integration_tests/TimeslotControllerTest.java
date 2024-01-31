package ru.ktelabs.schedule_service.integration_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.doctor.service.DoctorService;
import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;
import ru.ktelabs.schedule_service.error.exception.IncorrectRequestException;
import ru.ktelabs.schedule_service.error.exception.NotFoundException;
import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;
import ru.ktelabs.schedule_service.patient.dto.PatientIdentifierDto;
import ru.ktelabs.schedule_service.patient.service.PatientService;
import ru.ktelabs.schedule_service.timeslot.controller.TimeslotController;
import ru.ktelabs.schedule_service.timeslot.dto.NewScheduleDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotFullDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotMapper;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.timeslot.repository.TimeslotRepository;
import ru.ktelabs.schedule_service.timeslot.service.TimeslotService;
import ru.ktelabs.schedule_service.util.CustomFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimeslotControllerTest {

    private final TimeslotService timeslotService;
    private final TimeslotController timeslotController;
    private final TimeslotRepository timeslotRepository;
    private static final String DATE_STRING = CustomFormatter.dateToString(LocalDate.now().plusDays(10));
    private static final String START_TIME_STRING = "10:00";
    private static final int DURATION = 60;
    private static final int AMOUNT = 5;
    private static NewScheduleDto newScheduleDto;
    private final DoctorService doctorService;
    private static Long doctorId;
    private final PatientService patientService;
    private static Long patientId;
    private static UUID patientUuid;

    @BeforeEach
    void setUp() {

        NewDoctorDto properNewDoctorDto = NewDoctorDto.builder()
                .firstName("ProperFirstName")
                .secondName("ProperSecondName")
                .lastName("ProperLastName")
                .build();

        doctorId = doctorService.addDoctor(properNewDoctorDto).getId();

        newScheduleDto = NewScheduleDto.builder()
                .doctorId(doctorId)
                .date(DATE_STRING)
                .startTime(START_TIME_STRING)
                .duration(DURATION)
                .amount(AMOUNT)
                .build();

        NewPatientDto properNewPatientDto = NewPatientDto.builder()
                .firstName("ProperFirstName")
                .secondName("ProperSecondName")
                .lastName("ProperLastName")
                .birthDate("01.01.1990")
                .build();

        PatientFullDto patient = patientService.addPatient(properNewPatientDto);
        patientId = patient.getId();
        patientUuid = patient.getUuid();
    }

    @Test
    void shouldAddNewSchedule() {

        timeslotService.addTimeslots(newScheduleDto);

        List<Timeslot> foundSlots = timeslotRepository.findAll();

        foundSlots.sort(Comparator.comparing(Timeslot::getId));

        LocalTime inputStartTime = CustomFormatter.stringToTime(START_TIME_STRING);
        assert inputStartTime != null;

        assertEquals(AMOUNT, foundSlots.size());
        assertEquals(CustomFormatter.stringToDate(DATE_STRING), foundSlots.get(0).getDate());
        assertEquals(inputStartTime, foundSlots.get(0).getStartTime());
        assertEquals(inputStartTime.plusMinutes(DURATION * (AMOUNT - 1)),
                foundSlots.get(foundSlots.size() - 1).getStartTime());

        // add 1 slot to 60 minutes before start time
        LocalTime hourBeforeInputStartTime = inputStartTime.minusMinutes(60);
        NewScheduleDto scheduleWithHourBeforeStartTime = NewScheduleDto.builder()
                .doctorId(doctorId)
                .date(DATE_STRING)
                .startTime(CustomFormatter.timeToString(hourBeforeInputStartTime))
                .duration(DURATION)
                .amount(1)
                .build();

        timeslotService.addTimeslots(scheduleWithHourBeforeStartTime);

        foundSlots = timeslotRepository.findAll();

        foundSlots.sort(Comparator.comparing(Timeslot::getStartTime));

        assertEquals(AMOUNT + 1, foundSlots.size());
        assertEquals(hourBeforeInputStartTime,
                foundSlots.get(0).getStartTime());

        // error with same start time
        NewScheduleDto scheduleWithSameStartTime = NewScheduleDto.builder()
                .doctorId(doctorId)
                .date(DATE_STRING)
                .startTime(START_TIME_STRING)
                .duration(DURATION)
                .amount(1)
                .build();

        assertThrows(ConflictOnRequestException.class, () -> timeslotService.addTimeslots(scheduleWithSameStartTime));

        // error with 1 minute before start time
        LocalTime incorrectTime = inputStartTime.minusMinutes(1);

        NewScheduleDto scheduleWithStartTimeInTheMiddle = NewScheduleDto.builder()
                .doctorId(doctorId)
                .date(DATE_STRING)
                .startTime(CustomFormatter.timeToString(incorrectTime))
                .duration(DURATION)
                .amount(1)
                .build();

        assertThrows(ConflictOnRequestException.class, () ->
                timeslotService.addTimeslots(scheduleWithStartTimeInTheMiddle));

        // error with old date
        LocalDate oldDate = LocalDate.now().minusDays(1);
        String oldDateString = CustomFormatter.dateToString(oldDate);

        NewScheduleDto scheduleWithOldDate = NewScheduleDto.builder()
                .doctorId(doctorId)
                .date(oldDateString)
                .startTime(START_TIME_STRING)
                .duration(DURATION)
                .amount(1)
                .build();

        assertThrows(ConflictOnRequestException.class, () -> timeslotService.addTimeslots(scheduleWithOldDate));
    }

    @Test
    void shouldGetEmptySlotsByDoctorIdAndDate() {

        timeslotService.addTimeslots(newScheduleDto);

        List<Timeslot> initialSlots = timeslotRepository.findAll();

        initialSlots.sort(Comparator.comparing(Timeslot::getStartTime));

        List<TimeslotFullDto> initialSlotsDto = TimeslotMapper.modelToFullDto(initialSlots);

        List<TimeslotFullDto> foundSlots = timeslotController.getEmptySlotsByDoctorIdAndDate(doctorId, DATE_STRING);

        assertEquals(initialSlotsDto, foundSlots);
    }

    @Test
    void shouldReserveByIdAndThenCleanById() {

        timeslotService.addTimeslots(newScheduleDto);

        List<Timeslot> initialSlots = timeslotRepository.findAll();

        Long slotId = initialSlots.get(0).getId();

        assertTrue(timeslotController.getEmptySlotsByDoctorIdAndDate(doctorId, DATE_STRING).stream()
                .map(TimeslotFullDto::getId).toList().contains(slotId));

        // error with incorrect ids
        assertThrows(NotFoundException.class, () ->
                timeslotController.reserveById(slotId - 999L, patientId));

        assertThrows(NotFoundException.class, () ->
                timeslotController.reserveById(slotId, patientId - 999L));

        // should reserve by slot id
        TimeslotFullDto dtoToCheck = timeslotController.reserveById(slotId, patientId);

        Timeslot changedInitialSlot = timeslotRepository.findById(slotId).get();
        TimeslotFullDto changedInitialSlotDto = TimeslotMapper.modelToFullDto(changedInitialSlot);

        assertEquals(changedInitialSlotDto, dtoToCheck);

        assertFalse(timeslotController.getEmptySlotsByDoctorIdAndDate(doctorId, DATE_STRING).stream()
                .map(TimeslotFullDto::getId).toList().contains(slotId));

        // error with same slot
        assertThrows(ConflictOnRequestException.class, () -> timeslotController.reserveById(slotId, patientId));

        // should clean by id
        dtoToCheck = timeslotController.cleanById(slotId);

        changedInitialSlot = timeslotRepository.findById(slotId).get();
        changedInitialSlotDto = TimeslotMapper.modelToFullDto(changedInitialSlot);

        assertEquals(changedInitialSlotDto, dtoToCheck);

        assertTrue(timeslotController.getEmptySlotsByDoctorIdAndDate(doctorId, DATE_STRING).stream()
                .map(TimeslotFullDto::getId).toList().contains(slotId));

        // error with same slot
        assertThrows(IncorrectRequestException.class, () -> timeslotController.cleanById(slotId));
    }

    @Test
    void shouldGetSlotsByPatientIdOrUuid() {

        timeslotService.addTimeslots(newScheduleDto);

        List<Timeslot> slots = timeslotRepository.findAll();

        Long slot1Id = slots.get(0).getId();

        TimeslotFullDto firstSlotDto = timeslotController.reserveById(slot1Id, patientId);

        List<TimeslotFullDto> listToCheck =
                timeslotController.getSlotsByPatientIdOrUuid(
                        new PatientIdentifierDto(patientId, null), 0, 10);

        assertEquals(1, listToCheck.size());
        assertEquals(firstSlotDto, listToCheck.get(0));

        NewScheduleDto anotherScheduleDto = NewScheduleDto.builder()
                .doctorId(doctorId)
                .date(CustomFormatter.dateToString(LocalDate.now().plusDays(20)))
                .startTime(START_TIME_STRING)
                .duration(DURATION)
                .amount(AMOUNT)
                .build();

        timeslotService.addTimeslots(anotherScheduleDto);

        slots = timeslotRepository.findAll();

        Long slot2Id = slots.get(slots.size() - 1).getId();

        TimeslotFullDto secondSlotDto = timeslotController.reserveById(slot2Id, patientId);

        listToCheck =
                timeslotController.getSlotsByPatientIdOrUuid(
                        new PatientIdentifierDto(null, patientUuid), 0, 10);

        assertEquals(2, listToCheck.size());
        assertEquals(secondSlotDto, listToCheck.get(listToCheck.size() - 1));

    }

    @Test
    void shouldRemoveById() {

        timeslotService.addTimeslots(newScheduleDto);

        List<Timeslot> initialSlots = timeslotRepository.findAll();

        Long slotId = initialSlots.get(0).getId();

        assertTrue(initialSlots.stream()
                .map(Timeslot::getId).toList().contains(slotId));

        timeslotController.removeById(slotId);

        List<Timeslot> changedInitialSlots = timeslotRepository.findAll();

        assertFalse(changedInitialSlots.stream()
                .map(Timeslot::getId).toList().contains(slotId));


    }
}
