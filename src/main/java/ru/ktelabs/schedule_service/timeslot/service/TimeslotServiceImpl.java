package ru.ktelabs.schedule_service.timeslot.service;


import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ktelabs.schedule_service.doctor.model.Doctor;
import ru.ktelabs.schedule_service.doctor.repository.DoctorRepository;
import ru.ktelabs.schedule_service.error.exception.ConflictOnRequestException;
import ru.ktelabs.schedule_service.error.exception.IncorrectRequestException;
import ru.ktelabs.schedule_service.error.exception.NotFoundException;
import ru.ktelabs.schedule_service.patient.dto.PatientIdentifierDto;
import ru.ktelabs.schedule_service.patient.model.Patient;
import ru.ktelabs.schedule_service.patient.repository.PatientRepository;
import ru.ktelabs.schedule_service.timeslot.dto.NewScheduleDto;
import ru.ktelabs.schedule_service.timeslot.dto.ScheduleResponseDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotFullDto;
import ru.ktelabs.schedule_service.timeslot.dto.TimeslotMapper;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.timeslot.repository.TimeslotRepository;
import ru.ktelabs.schedule_service.timeslot.service.util.TimeslotsCreator;
import ru.ktelabs.schedule_service.util.CustomFormatter;
import ru.ktelabs.schedule_service.util.PageRequestCreator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TimeslotServiceImpl implements TimeslotService {

    private final TimeslotRepository timeslotRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final EntityManager entityManager;
    private static final int BATCH_SIZE = 10;

    @Override
    @Transactional
    public ScheduleResponseDto addTimeslots(NewScheduleDto newScheduleDto) {

        log.info("-- Saving new schedule: {}", newScheduleDto);

        Doctor doctor = doctorRepository.findById(newScheduleDto.getDoctorId()).orElseThrow(() ->
                new NotFoundException("- DoctorId not found: " + newScheduleDto.getDoctorId()));

        int createdTimeslotsAmount = 0;

        // batch insert created timeslots to database
        for (Timeslot timeslot : TimeslotsCreator.create(timeslotRepository, newScheduleDto, doctor)) {

            if (createdTimeslotsAmount > 0 && createdTimeslotsAmount % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }

            entityManager.persist(timeslot);

            createdTimeslotsAmount++;
        }

        ScheduleResponseDto dtoToReturn = ScheduleResponseDto.builder()
                .doctorId(doctor.getId())
                .date(newScheduleDto.getDate())
                .startTime(newScheduleDto.getStartTime())
                .duration(newScheduleDto.getDuration())
                .amount(createdTimeslotsAmount)
                .build();

        log.info("-- Schedule has been saved: doctor={}, date={}, created slots amount={}",
                doctor.getLastName() + " " + doctor.getFirstName().charAt(0) + ".",
                newScheduleDto.getDate(),
                createdTimeslotsAmount);

        return dtoToReturn;
    }

    @Override
    public List<TimeslotFullDto> getEmptySlotsByDoctorIdAndDate(Long doctorId, String dateString) {

        log.info("-- Returning empty slots for doctorId={}, date={}", doctorId, dateString);

        if (!doctorRepository.existsById(doctorId)) {
            throw new NotFoundException("- DoctorId not found: " + doctorId);
        }

        LocalDate date;
        date = CustomFormatter.stringToDate(dateString);
        assert date != null;
        if (date.isBefore(LocalDate.now())) {
            throw new ConflictOnRequestException("- Date cannot be before current date");
        }

        List<TimeslotFullDto> listToReturn = new ArrayList<>();

        List<Timeslot> existedSlots = timeslotRepository.findAllByDoctorIdAndDate(doctorId, date);

        for (Timeslot existedSlot : existedSlots) {

            if (existedSlot.getPatient() == null) {

                listToReturn.add(TimeslotMapper.modelToFullDto(existedSlot));
            }
        }

        log.info("-- List of empty slots returned, size={}", listToReturn.size());

        return listToReturn;
    }

    @Override
    @Transactional
    public TimeslotFullDto reserveById(Long timeslotId, Long patientId) {

        log.info("-- Reserving timeslot by id={} for patientId={}", timeslotId, patientId);

        Timeslot timeslot = timeslotRepository.findById(timeslotId).orElseThrow(() ->
                new NotFoundException("- TimeslotId not found: " + timeslotId));

        if (timeslot.getPatient() != null) {
            throw new ConflictOnRequestException("- Slot already busy by patientId=" + timeslot.getPatient().getId());
        }

        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new NotFoundException("- PatientId not found: " + patientId));

        timeslot.setPatient(patient);

        TimeslotFullDto fullDtoToReturn = TimeslotMapper.modelToFullDto(timeslotRepository.save(timeslot));

        log.info("-- Timeslot has been reserved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public TimeslotFullDto cleanById(Long timeslotId) {

        log.info("-- Cleaning timeslot by id={}", timeslotId);

        Timeslot timeslot = timeslotRepository.findById(timeslotId).orElseThrow(() ->
                new NotFoundException("- TimeslotId not found: " + timeslotId));

        if (timeslot.getPatient() == null) {
            throw new IncorrectRequestException("- Slot already free to reserve");
        }

        timeslot.setPatient(null);

        TimeslotFullDto fullDtoToReturn = TimeslotMapper.modelToFullDto(timeslotRepository.save(timeslot));

        log.info("-- Timeslot now is free to reserve: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public List<TimeslotFullDto> getSlotsByPatientIdOrUuid(PatientIdentifierDto patientIdentifierDto,
                                                           int from, int size) {

        log.info("-- Returning timeslots by PatientIdentifier={}", patientIdentifierDto);

        List<TimeslotFullDto> listToReturn;
        Iterable<Timeslot> foundSlots;

        Sort sort = Sort.by("date").and(Sort.by("startTime"));
        PageRequest pageRequest = PageRequestCreator.create(from, size, sort);

        if (patientIdentifierDto.getId() != null) {
            foundSlots = timeslotRepository.findByPatientId(patientIdentifierDto.getId(), pageRequest);
        } else {
            foundSlots = timeslotRepository.findByPatientUuid(patientIdentifierDto.getUuid(), pageRequest);
        }

        listToReturn = TimeslotMapper.modelToFullDto(foundSlots);

        log.info("-- List of slots by PatientIdentifier={} returned, size={}",
                patientIdentifierDto, listToReturn.size());

        return listToReturn;

    }

    @Override
    @Transactional
    public void removeById(Long timeslotId) {

        log.info("-- Deleting timeslot by timeslotId={}", timeslotId);

        Timeslot timeslotToDelete = timeslotRepository.findById(timeslotId).orElseThrow(() ->
                new NotFoundException("- TimeslotId not found: " + timeslotId));

        TimeslotFullDto dtoToShowInLog = TimeslotMapper.modelToFullDto(timeslotToDelete);

        timeslotRepository.deleteById(timeslotId);

        log.info("-- Timeslot deleted: {}", dtoToShowInLog);
    }
}
