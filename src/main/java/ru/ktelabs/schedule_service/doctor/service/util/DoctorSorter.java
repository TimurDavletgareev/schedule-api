package ru.ktelabs.schedule_service.doctor.service.util;

import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class DoctorSorter {

    public static Sort createSort(String sortBy) {

        if (Arrays.stream(DoctorSortOption.values())
                .map(Enum::name)
                .toList()
                .contains(sortBy.toUpperCase())) {

            sortBy = sortBy.toLowerCase();

        } else {

            sortBy = "id";
        }

        return Sort.by(sortBy);
    }

    public enum DoctorSortOption {

        FIRST_NAME,
        SECOND_NAME,
        LAST_NAME
    }
}
