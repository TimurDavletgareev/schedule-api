package ru.ktelabs.schedule_service.timeslot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    List<Timeslot> findAllByDoctorIdAndDate(Long doctorId, LocalDate date);

    Page<Timeslot> findByPatientId(Long id, PageRequest pageRequest);

    Page<Timeslot> findByPatientUuid(UUID uuid, PageRequest pageRequest);
}
