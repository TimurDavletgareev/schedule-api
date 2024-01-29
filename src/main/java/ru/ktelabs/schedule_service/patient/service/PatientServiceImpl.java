package ru.ktelabs.schedule_service.patient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.schedule_service.error.exception.NotFoundException;
import ru.ktelabs.schedule_service.patient.dto.NewPatientDto;
import ru.ktelabs.schedule_service.patient.dto.PatientFullDto;
import ru.ktelabs.schedule_service.patient.dto.PatientMapper;
import ru.ktelabs.schedule_service.patient.dto.PatientUpdateDto;
import ru.ktelabs.schedule_service.patient.model.Patient;
import ru.ktelabs.schedule_service.patient.repository.PatientRepository;
import ru.ktelabs.schedule_service.patient.service.util.PatientChecker;
import ru.ktelabs.schedule_service.patient.service.util.PatientCreator;
import ru.ktelabs.schedule_service.patient.service.util.PatientUpdater;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientFullDto addPatient(NewPatientDto newPatientDto) {

        log.info("-- Saving patient: {}", newPatientDto);

        Patient patientToSave = PatientCreator.createFromNewPatientDto(newPatientDto);

        PatientChecker.checkPatient(patientRepository, patientToSave);

        PatientFullDto fullDtoToReturn = PatientMapper.modelToFullDto(patientRepository.save(patientToSave));

        log.info("-- Patient has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public PatientFullDto updatePatient(Long patientId, PatientUpdateDto patientUpdateDto) {

        log.info("-- Updating patient by id={}", patientId);

        Patient patientToUpdate = patientRepository.findById(patientId).orElseThrow(() ->
                new NotFoundException("- PatientId not found: " + patientId));

        Patient updatedPatient = PatientUpdater.update(patientToUpdate, patientUpdateDto);

        PatientChecker.checkPatient(patientRepository, updatedPatient);

        PatientFullDto fullDtoToReturn = PatientMapper.modelToFullDto(patientRepository.save(updatedPatient));

        log.info("-- Patient has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public PatientFullDto getById(Long patientId) {

        log.info("-- Returning doctor by patientId={}", patientId);

        PatientFullDto fullDtoToReturn =
                PatientMapper.modelToFullDto(patientRepository.findById(patientId).orElseThrow(() ->
                        new NotFoundException("- PatientId not found: " + patientId)));

        log.info("-- Doctor returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long patientId) {

        log.info("-- Deleting patient by patientId={}", patientId);

        Patient patientToCheck = patientRepository.findById(patientId).orElseThrow(() ->
                new NotFoundException("- PatientId not found: " + patientId));

        PatientFullDto dtoToShowInLog = PatientMapper.modelToFullDto(patientToCheck);

        patientRepository.deleteById(patientId);

        log.info("-- Patient deleted: {}", dtoToShowInLog);
    }
}
