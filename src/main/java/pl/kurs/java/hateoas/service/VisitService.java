package pl.kurs.java.hateoas.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.hateoas.exceptions.DoctorNotFoundException;
import pl.kurs.java.hateoas.exceptions.IncorrectEntityDataException;
import pl.kurs.java.hateoas.exceptions.PatientNotFoundException;
import pl.kurs.java.hateoas.model.Visit;
import pl.kurs.java.hateoas.repository.DoctorRepository;
import pl.kurs.java.hateoas.repository.PatientRepository;
import pl.kurs.java.hateoas.repository.VisitRepository;

@Service
@RequiredArgsConstructor
public class VisitService {

	private final VisitRepository visitRepository;
	private final DoctorRepository doctorRepository;
	private final PatientRepository patientRepository;
	
	public List<Visit> loadVisits(List<String[]> data) {
		List<Visit> visits = new ArrayList<Visit>();
		
		try {
			data.stream().forEach(row -> 
			visits.add(Visit.builder()
						  	.doctor(doctorRepository.findById(Long.valueOf(row[0]))
						  		.orElseThrow(() -> 
						  		new DoctorNotFoundException(Long.valueOf(row[0]))))
						  	.patient(patientRepository.findById(Long.valueOf(row[1]))
								.orElseThrow(() -> 
								new PatientNotFoundException(Long.valueOf(row[1]))))
						  	.date(Date.valueOf(row[2]))
						  	.build()));
		}
		catch (IndexOutOfBoundsException | NumberFormatException e) {
			throw new IncorrectEntityDataException(Visit.class);
		}
		
		return visitRepository.saveAll(visits);
	}
	
	public List<Visit> getAllVisits() {
		return visitRepository.findAll();
	}
	
	public void deleteAllVisits() {
		visitRepository.deleteAll();
	}

	public Page<Visit> getAllVisits(Pageable pageable) {
		return visitRepository.findAll(pageable);
	}

	public List<String> exportVisits() {
		return getAllVisits().stream().map(d -> d.toString())
									   .collect(Collectors.toList());
	}
}
