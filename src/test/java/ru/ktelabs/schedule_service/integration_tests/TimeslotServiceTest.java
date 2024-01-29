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
import ru.ktelabs.schedule_service.timeslot.dto.NewScheduleDto;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.timeslot.repository.TimeslotRepository;
import ru.ktelabs.schedule_service.timeslot.service.TimeslotService;
import ru.ktelabs.schedule_service.util.CustomFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TimeslotServiceTest {

    private final TimeslotService timeslotService;
    private final TimeslotRepository timeslotRepository;
    private static final String DATE_STRING = CustomFormatter.dateToString(LocalDate.now().plusDays(10));
    private static final String START_TIME_STRING = "10:00";
    private static final int DURATION = 60;
    private static final int AMOUNT = 5;
    private static NewScheduleDto newScheduleDto;
    private final DoctorService doctorService;
    private static Long doctorId;

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

        foundSlots.sort(Comparator.comparing(Timeslot::getId));

        assertEquals(AMOUNT + 1, foundSlots.size());
        assertEquals(hourBeforeInputStartTime,
                foundSlots.get(foundSlots.size() - 1).getStartTime());

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
    }
}
