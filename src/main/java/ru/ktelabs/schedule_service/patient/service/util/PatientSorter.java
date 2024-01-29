package ru.ktelabs.schedule_service.patient.service.util;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class PatientSorter {

    public static Sort createSort(String sortBy) {

        if (Arrays.stream(PatientSortOption.values())
                .map(Enum::name)
                .toList()
                .contains(sortBy.toUpperCase())) {

            sortBy = sortBy.toLowerCase();

        } else {

            sortBy = "id";
        }

        return Sort.by(sortBy);
    }

    public enum PatientSortOption {

        FIRST_NAME,
        SECOND_NAME,
        LAST_NAME
    }
}
