package ru.ktelabs.schedule_service.doctor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewDoctorDto {

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
}
