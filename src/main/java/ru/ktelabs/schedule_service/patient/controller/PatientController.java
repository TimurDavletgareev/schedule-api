package ru.ktelabs.schedule_service.patient.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;
import ru.ktelabs.schedule_service.patient.dto.PatientUpdateDto;
import ru.ktelabs.schedule_service.patient.service.PatientService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/patients")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientFullDto addPatient(@RequestBody @Valid NewPatientDto newPatientDto) {

        return patientService.addPatient(newPatientDto);
    }

    @PatchMapping("/{patientId}")
    public PatientFullDto updatePatient(@PathVariable @NotNull Long patientId,
                                        @RequestBody @Valid PatientUpdateDto inputDto) {

        return patientService.updatePatient(patientId, inputDto);
    }

    @GetMapping("/{patientId}")
    public PatientFullDto getById(@PathVariable @NotNull Long patientId) {

        return patientService.getById(patientId);
    }

    @DeleteMapping("/{patientId}")
    public void removeById(@PathVariable @NotNull Long patientId) {

        patientService.removeById(patientId);
    }
}
