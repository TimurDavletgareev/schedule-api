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

            for (int i = 0; i < sortBy.length(); i++) {

                if (sortBy.charAt(i) == '_') {

                    String newBeginning = sortBy.substring(0, i);
                    String newEnding = sortBy.substring(i + 2);
                    String letterToUpperCase = sortBy.substring(i + 1, i + 2).toUpperCase();
                    sortBy = newBeginning + letterToUpperCase + newEnding;
                    break;
                }
            }

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
