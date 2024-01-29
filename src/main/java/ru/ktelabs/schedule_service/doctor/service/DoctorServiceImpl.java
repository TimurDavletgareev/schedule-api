package ru.ktelabs.schedule_service.doctor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.schedule_service.doctor.dto.DoctorFullDto;
import ru.ktelabs.schedule_service.doctor.dto.DoctorMapper;
import ru.ktelabs.schedule_service.doctor.dto.DoctorUpdateDto;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.doctor.repository.DoctorRepository;
import ru.ktelabs.schedule_service.doctor.service.util.DoctorChecker;
import ru.ktelabs.schedule_service.doctor.service.util.DoctorCreator;
import ru.ktelabs.schedule_service.doctor.service.util.DoctorSorter;
import ru.ktelabs.schedule_service.doctor.service.util.DoctorUpdater;
import ru.ktelabs.schedule_service.error.exception.NotFoundException;
import ru.ktelabs.schedule_service.util.PageRequestCreator;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public DoctorFullDto addDoctor(NewDoctorDto newDoctorDto) {

        log.info("-- Saving doctor: {}", newDoctorDto);

        Doctor doctorToSave = DoctorCreator.createFromNewDoctorDto(newDoctorDto);

        DoctorChecker.check(doctorRepository, doctorToSave);

        DoctorFullDto fullDtoToReturn = DoctorMapper.modelToFullDto(doctorRepository.save(doctorToSave));

        log.info("-- Doctor has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public DoctorFullDto updateDoctor(Long doctorId, DoctorUpdateDto doctorUpdateDto) {

        log.info("-- Updating doctor by id={}", doctorId);

        Doctor doctorToUpdate = doctorRepository.findById(doctorId).orElseThrow(() ->
                new NotFoundException("- DoctorId not found: " + doctorId));

        Doctor updatedDoctor = DoctorUpdater.update(doctorToUpdate, doctorUpdateDto);


        DoctorChecker.check(doctorRepository, updatedDoctor);

        DoctorFullDto fullDtoToReturn = DoctorMapper.modelToFullDto(doctorRepository.save(updatedDoctor));

        log.info("-- Doctor has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public DoctorFullDto getById(Long doctorId) {

        log.info("-- Returning doctor by doctorId={}", doctorId);

        DoctorFullDto fullDtoToReturn =
                DoctorMapper.modelToFullDto(doctorRepository.findById(doctorId).orElseThrow(() ->
                        new NotFoundException("- DoctorId not found: " + doctorId)));

        log.info("-- Doctor returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public List<DoctorFullDto> getAll(String sortBy, int from, int size) {

        log.info("-- Returning all doctors sorted by {}", sortBy);

        Sort sort = DoctorSorter.createSort(sortBy);
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        Iterable<Doctor> foundDoctors = doctorRepository.findAll(pageRequest);

        List<DoctorFullDto> listToReturn = DoctorMapper.modelToFullDto(foundDoctors);

        log.info("-- Doctor list returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long doctorId) {

        log.info("-- Deleting doctor by doctorId={}", doctorId);

        Doctor userToCheck = doctorRepository.findById(doctorId).orElseThrow(() ->
                new NotFoundException("- DoctorId not found: " + doctorId));

        DoctorFullDto dtoToShowInLog = DoctorMapper.modelToFullDto(userToCheck);

        doctorRepository.deleteById(doctorId);

        log.info("-- Doctor deleted: {}", dtoToShowInLog);
    }
}
