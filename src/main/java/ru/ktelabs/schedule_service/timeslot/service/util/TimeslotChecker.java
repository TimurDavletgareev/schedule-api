package ru.ktelabs.schedule_service.timeslot.service.util;

import org.springframework.stereotype.Component;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.util.Constants;
import ru.ktelabs.schedule_service.util.CustomFormatter;

import java.time.LocalTime;
import java.util.List;

@Component
public class TimeslotChecker {

    public static boolean isCorrectNewSlot(List<Timeslot> existedSlots,
                                           Timeslot slotToCheck) {

        if (!existedSlots.isEmpty()) {

            for (Timeslot existedSlot : existedSlots) {

                LocalTime startTimeToCheck = slotToCheck.getStartTime();
                LocalTime endTimeToCheck = slotToCheck.getStartTime().plusMinutes(slotToCheck.getDuration());
                LocalTime openHour = CustomFormatter.stringToTime(Constants.OPEN_HOUR_STRING);
                LocalTime closeHour = CustomFormatter.stringToTime(Constants.CLOSE_HOUR_STRING);

                assert openHour != null;
                assert closeHour != null;
                if (startTimeToCheck.isBefore(openHour)
                        || endTimeToCheck.isAfter(closeHour)) {
                    return false;
                }

                LocalTime lockedStartTime = existedSlot.getStartTime();
                LocalTime lockedEndTime = existedSlot.getStartTime().plusMinutes(existedSlot.getDuration());

                if (!startTimeToCheck.isBefore(lockedStartTime) && startTimeToCheck.isBefore(lockedEndTime)
                        || (endTimeToCheck.isAfter(lockedStartTime) && endTimeToCheck.isBefore(lockedEndTime))) {
                    return false;
                }
            }
        }

        return true;
    }
}
