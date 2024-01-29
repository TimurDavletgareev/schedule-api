package ru.ktelabs.schedule_service.timeslot.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ktelabs.schedule_service.patient.dto.PatientIdentifierDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotFullDto;
import ru.ktelabs.schedule_service.timeslot.service.TimeslotService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/schedule")
public class TimeslotController {

    private final TimeslotService timeslotService;

    @GetMapping("/doctor/{doctorId}")
    public List<TimeslotFullDto> getEmptySlotsByDoctorIdAndDate(
            @PathVariable @NotNull Long doctorId,
            @RequestParam(value = "date", required = false) String date) {

        return timeslotService.getEmptySlotsByDoctorIdAndDate(doctorId, date);
    }

    @PatchMapping("/reserve")
    public TimeslotFullDto reserveById(@RequestParam(value = "timeslot_id") Long timeslotId,
                                       @RequestParam(value = "patient_id_id") Long patientId) {

        return timeslotService.reserveById(timeslotId, patientId);
    }

    @PatchMapping("/clean/{timeslotId}")
    public TimeslotFullDto cleanById(@PathVariable @NotNull Long timeslotId) {

        return timeslotService.cleanById(timeslotId);
    }

    @GetMapping("/patient")
    public List<TimeslotFullDto> getSlotsByPatientIdOrUuid(
            @RequestBody @Valid PatientIdentifierDto patientIdentifierDto) {

        return timeslotService.getSlotsByPatientIdOrUuid(patientIdentifierDto);
    }

    @DeleteMapping("/{timeslotId}")
    public void removeById(@PathVariable @NotNull Long timeslotId) {

        timeslotService.removeById(timeslotId);
    }
}
