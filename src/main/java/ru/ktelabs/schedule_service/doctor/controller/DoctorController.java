package ru.ktelabs.schedule_service.doctor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ktelabs.schedule_service.doctor.dto.DoctorFullDto;
import ru.ktelabs.schedule_service.doctor.dto.DoctorUpdateDto;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.doctor.service.DoctorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/doctors")
@Validated
@Tag(name = "Врачи", description = "Взаимодействие с докторами")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Добавление нового доктора"
    )
    public DoctorFullDto addDoctor(
            @RequestBody @Valid
            @Parameter(description = "DTO нового врача")
            NewDoctorDto newDoctorDto
    ) {

        return doctorService.addDoctor(newDoctorDto);
    }

    @PatchMapping("/{doctorId}")
    @Operation(
            summary = "Изменение доктора"
    )
    public DoctorFullDto updateDoctor(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор врача")
            Long doctorId,
            @RequestBody @Valid
            @Parameter(description = "DTO обновления врача")
            DoctorUpdateDto inputDto
    ) {

        return doctorService.updateDoctor(doctorId, inputDto);
    }

    @GetMapping("/{doctorId}")
    @Operation(
            summary = "Получение доктора по id"
    )
    public DoctorFullDto getById(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор врача")
            Long doctorId
    ) {

        return doctorService.getById(doctorId);
    }

    @GetMapping
    @Operation(
            summary = "Получение списка всех докторов",
            description = "Список всех докторов постранично с сортировкой по id/ФИО"
    )
    public List<DoctorFullDto> getAll(
            @RequestParam(value = "sortBy", required = false, defaultValue = "id")
            @Parameter(description = "Тип сортировки списка", example = "first_name")
            String sortBy,
            @RequestParam(value = "from", defaultValue = "0")
            @Parameter(description = "Номер первой отображаемой записи списка")
            int from,
            @RequestParam(value = "size", defaultValue = "10")
            @Parameter(description = "Количество записей списка, отображаемых на одной странице")
            int size
    ) {

        return doctorService.getAll(sortBy, from, size);
    }

    @DeleteMapping("/{doctorId}")
    @Operation(
            summary = "Удаление доктора по id"
    )
    public void removeById(
            @PathVariable @NotNull
            @Parameter(description = "Идентификатор врача")
            Long doctorId
    ) {

        doctorService.removeById(doctorId);
    }
}
