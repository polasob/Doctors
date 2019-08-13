package pl.kurs.java.hateoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kurs.java.hateoas.model.Patient;


public interface PatientRepository extends JpaRepository<Patient, Long> {

}
