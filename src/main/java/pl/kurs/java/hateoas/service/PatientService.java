package pl.kurs.java.hateoas.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.hateoas.exceptions.IncorrectEntityDataException;
import pl.kurs.java.hateoas.model.Patient;
import pl.kurs.java.hateoas.repository.PatientRepository;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepository patientRepository;
	
	public List<Patient> loadPatients(List<String[]> data) {
		List<Patient> patients = new ArrayList<Patient>();
		
		try {
			data.stream().forEach(row -> patients.add(Patient.builder()
			  		.id(Long.valueOf(row[0]))
			  		.lastName(row[1])
			  		.firstName(row[2])
			  		.pesel(row[3])
			  		.dateOfBirth(Date.valueOf(row[4]))
			  		.build()));
		}
		catch (IndexOutOfBoundsException | NumberFormatException e) {
			throw new IncorrectEntityDataException(Patient.class);
		}
		
		return patientRepository.saveAll(patients);
	}
	
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}
	
	public void deleteAllPatients() {
		patientRepository.deleteAll();
	}

	public Page<Patient> getAllPatients(Pageable pageable) {
		return patientRepository.findAll(pageable);
	}

	public List<String> exportPatients() {
		return getAllPatients().stream().map(d -> d.toString())
									   .collect(Collectors.toList());
	}
}
