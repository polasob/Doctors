package pl.kurs.java.hateoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.kurs.java.hateoas.model.Visit;


public interface VisitRepository extends JpaRepository<Visit, Long> {

}
