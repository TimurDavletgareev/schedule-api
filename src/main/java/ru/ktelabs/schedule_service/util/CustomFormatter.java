package ru.ktelabs.schedule_service.util;

import ru.ktelabs.schedule_service.error.exception.IncorrectRequestException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);

    public static LocalDate stringToDate(String dateString) {

        try {
            if (dateString != null) {
                return LocalDate.parse(dateString, DATE_FORMATTER);
            } else {
                return null;
            }
        } catch (DateTimeParseException e) {
            throw new IncorrectRequestException("- Wrong date format, must be: " + Constants.DATE_FORMAT);
        }
    }

    public static String dateToString(LocalDate date) {

        if (date != null) {
            return date.format(DATE_FORMATTER);
        } else {
            return null;
        }
    }

    public static LocalTime stringToTime(String dateString) {

        try {
            if (dateString != null) {
                return LocalTime.parse(dateString, TIME_FORMATTER);
            } else {
                return null;
            }
        } catch (DateTimeParseException e) {
            throw new IncorrectRequestException("- Wrong time format, must be: " + Constants.TIME_FORMAT);
        }
    }

    public static String timeToString(LocalTime dateTime) {

        return dateTime.format(TIME_FORMATTER);
    }
}
