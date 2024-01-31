package ru.ktelabs.schedule_service.timeslot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ktelabs.schedule_service.patient.dto.PatientIdentifierDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotFullDto;
import ru.ktelabs.schedule_service.timeslot.service.TimeslotService;
import ru.ktelabs.schedule_service.util.Constants;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/schedules")
@Tag(name = "Расписания", description = "Взаимодействие с расписаниями")
public class TimeslotController {

    private final TimeslotService timeslotService;

    @GetMapping("/doctor/{doctorId}")
    @Operation(
            summary = "Доступные слоты времени доктора",
            description = "Получение свободных слотов времени к указанному врачу на указанную дату"
    )
    public List<TimeslotFullDto> getEmptySlotsByDoctorIdAndDate(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор врача")
            Long doctorId,
            @RequestParam(value = "date")
            @Parameter(description = "Дата в формате " + Constants.DATE_FORMAT)
            String date
    ) {

        return timeslotService.getEmptySlotsByDoctorIdAndDate(doctorId, date);
    }

    @PatchMapping("/reserve")
    @Operation(
            summary = "Запись пациента на приём",
            description = "Занятие слота времени по его id"
    )
    public TimeslotFullDto reserveById(
            @Parameter(description = "Идентификатор слота времени")
            @RequestParam(value = "timeslotId")
            Long timeslotId,
            @Parameter(description = "Идентификатор пациента")
            @RequestParam(value = "patientId")
            Long patientId
    ) {

        return timeslotService.reserveById(timeslotId, patientId);
    }

    @PatchMapping("/clean/{timeslotId}")
    @Operation(
            summary = "Отмена записи на приём",
            description = "Освобождение слота времени по его id"
    )
    public TimeslotFullDto cleanById(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор слота времени")
            Long timeslotId
    ) {

        return timeslotService.cleanById(timeslotId);
    }

    @GetMapping("/patient")
    @Operation(
            summary = "Список записей пациента на приём",
            description = "Получение всех слотов времени, занятых одним пациентом по его id/uuid"
    )
    public List<TimeslotFullDto> getSlotsByPatientIdOrUuid(
            @RequestBody @Valid
            @Parameter(description = "Набор идентификаторов пациента")
            PatientIdentifierDto patientIdentifierDto,
            @RequestParam(value = "from", defaultValue = "0")
            @Parameter(description = "Номер первой отображаемой записи списка")
            int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Parameter(description = "Количество записей списка, отображаемых на одной странице")
            int size
    ) {

        return timeslotService.getSlotsByPatientIdOrUuid(patientIdentifierDto, from, size);
    }

    @DeleteMapping("/{timeslotId}")
    @Operation(
            summary = "Удаление слота времени по id"
    )
    public void removeById(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор слота времени")
            Long timeslotId
    ) {

        timeslotService.removeById(timeslotId);
    }
}
