package ru.ktelabs.schedule_service.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ktelabs.schedule_service.doctor.model.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByFirstNameAndSecondNameAndLastName(String firstName, String secondName, String lastName);
}
