package pl.kurs.java.hateoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kurs.java.hateoas.model.Doctor;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
