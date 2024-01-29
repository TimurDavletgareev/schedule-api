package ru.ktelabs.schedule_service.patient.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewPatientDto {

    @NotNull
    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 50, message = "size must be between 2 and 50")
    private final String firstName;

    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 50, message = "size must be between 2 and 50")
    private final String secondName;

    @NotNull
    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 50, message = "size must be between 2 and 50")
    private final String lastName;

    @NotNull
    @Size(min = 12, message = "size must be between 12 and 13")
    @Size(max = 13, message = "size must be between 12 and 13")
    private final String birthDate;
}
