package ru.ktelabs.schedule_service.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;
import ru.ktelabs.schedule_service.patient.dto.PatientUpdateDto;
import ru.ktelabs.schedule_service.patient.service.PatientService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/patients")
@Validated
@Tag(name = "Пациенты", description = "Взаимодействие с пациентами")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Добавление нового пациента"
    )
    public PatientFullDto addPatient(

            @RequestBody @Valid
            @Parameter(description = "DTO нового пациента")
            NewPatientDto newPatientDto
    ) {

        return patientService.addPatient(newPatientDto);
    }

    @PatchMapping("/{patientId}")
    @Operation(
            summary = "Изменение пациента"
    )
    public PatientFullDto updatePatient(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор пациента")
            Long patientId,
            @RequestBody @Valid
            @Parameter(description = "DTO обновления пациента")
            PatientUpdateDto inputDto
    ) {

        return patientService.updatePatient(patientId, inputDto);
    }

    @GetMapping("/{patientId}")
    @Operation(
            summary = "Изменение пациента по id"
    )
    public PatientFullDto getById(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор пациента")
            Long patientId
    ) {

        return patientService.getById(patientId);
    }

    @DeleteMapping("/{patientId}")
    @Operation(
            summary = "Удаление пациента по id"
    )
    public void removeById(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор пациента")
            Long patientId
    ) {

        patientService.removeById(patientId);
    }
}
