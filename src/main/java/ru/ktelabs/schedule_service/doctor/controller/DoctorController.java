package ru.ktelabs.schedule_service.doctor.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ktelabs.schedule_service.doctor.dto.DoctorFullDto;
import ru.ktelabs.schedule_service.doctor.dto.DoctorUpdateDto;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.doctor.service.DoctorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorFullDto addDoctor(@RequestBody @Valid NewDoctorDto newDoctorDto) {

        return doctorService.addDoctor(newDoctorDto);
    }

    @PatchMapping("/{doctorId}")
    public DoctorFullDto updateDoctor(@PathVariable @NotNull Long doctorId,
                                      @RequestBody @Valid DoctorUpdateDto inputDto) {

        return doctorService.updateDoctor(doctorId, inputDto);
    }

    @GetMapping("/{doctorId}")
    public DoctorFullDto getById(@PathVariable @NotNull Long doctorId) {

        return doctorService.getById(doctorId);
    }

    @GetMapping
    public List<DoctorFullDto> getAll(
            @RequestParam(value = "sortBy", required = false,
                    defaultValue = "id") String sortBy,
            @RequestParam(value = "from", defaultValue = "0") int from,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return doctorService.getAll(sortBy, from, size);
    }

    @DeleteMapping("/{doctorId}")
    public void removeById(@PathVariable @NotNull Long doctorId) {

        doctorService.removeById(doctorId);
    }
}
