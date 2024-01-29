package ru.ktelabs.schedule_service.timeslot.service.util;

import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;
import ru.ktelabs.schedule_service.timeslot.dto.NewScheduleDto;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.timeslot.repository.TimeslotRepository;
import ru.ktelabs.schedule_service.util.CustomFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeslotsCreator {

    public static List<Timeslot> create(TimeslotRepository timeslotRepository,
                                        NewScheduleDto newScheduleDto, Doctor doctor) {

        LocalDate date = CustomFormatter.stringToDate(newScheduleDto.getDate());
        LocalTime scheduleStartTime = CustomFormatter.stringToTime(newScheduleDto.getStartTime());
        LocalTime eachStartTime = scheduleStartTime;
        int duration = newScheduleDto.getDuration();

        List<Timeslot> existedSlots = timeslotRepository.findAllByDoctorIdAndDate(doctor.getId(), date);

        List<Timeslot> createdSlots = new ArrayList<>();

        for (int i = 0; i < newScheduleDto.getAmount(); i++) {

            Timeslot timeslot = new Timeslot();

            timeslot.setDoctor(doctor);
            timeslot.setDate(date);
            timeslot.setStartTime(eachStartTime);
            timeslot.setDuration(duration);

            if (!TimeslotChecker.isCorrectNewSlot(existedSlots, timeslot)) {
                throw new ConflictOnRequestException("- Slot in such time period already exists");
            }

            createdSlots.add(timeslot);

            assert eachStartTime != null;
            eachStartTime = eachStartTime.plusMinutes(duration);
        }

        return createdSlots;
    }
}
