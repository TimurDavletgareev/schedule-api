package ru.ktelabs.schedule_service.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.schedule_service.patient.model.Patient;

import java.time.LocalDate;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByFirstNameAndSecondNameAndLastNameAndBirthDate(
            String firstName, String secondName, String lastName, LocalDate birthDate);
}
