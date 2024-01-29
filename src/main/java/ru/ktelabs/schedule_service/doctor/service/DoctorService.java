package ru.ktelabs.schedule_service.doctor.service;

import ru.ktelabs.schedule_service.doctor.dto.DoctorFullDto;
import ru.ktelabs.schedule_service.doctor.dto.DoctorUpdateDto;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;

import java.util.List;

public interface DoctorService {

    DoctorFullDto addDoctor(NewDoctorDto newDoctorDto);

    DoctorFullDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto);

    DoctorFullDto getById(Long doctorId);

    List<DoctorFullDto> getAll(String sortBy, int from, int size);

    void removeById(Long doctorId);
}
