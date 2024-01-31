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
        assert date != null;
        LocalTime eachStartTime = CustomFormatter.stringToTime(newScheduleDto.getStartTime());
        assert eachStartTime != null;

        int duration = newScheduleDto.getDuration();

        List<Timeslot> existedSlots = timeslotRepository.findAllByDoctorIdAndDate(doctor.getId(), date);

        List<Timeslot> createdSlots = new ArrayList<>();

        for (int i = 0; i < newScheduleDto.getAmount(); i++) {

            Timeslot timeslot = new Timeslot();

            timeslot.setId(createId(doctor.getId(), date, eachStartTime));
            timeslot.setDoctor(doctor);
            timeslot.setDate(date);
            timeslot.setStartTime(eachStartTime);
            timeslot.setDuration(duration);

            if (!TimeslotChecker.isCorrectNewSlot(existedSlots, timeslot)) {
                throw new ConflictOnRequestException("- Slot in such time period already exists");
            }

            createdSlots.add(timeslot);


            eachStartTime = eachStartTime.plusMinutes(duration);
        }

        return createdSlots;
    }

    private static Long createId(Long doctorId, LocalDate date, LocalTime startTime) {

        String dateString = date.toString().replaceAll("-", "");
        String startTimeString = startTime.toString().replaceAll(":", "");
        String idString = doctorId + dateString + startTimeString;

        return Long.parseLong(idString);
    }
}
