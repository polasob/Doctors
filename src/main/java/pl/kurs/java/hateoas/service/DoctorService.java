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
import pl.kurs.java.hateoas.model.Doctor;
import pl.kurs.java.hateoas.repository.DoctorRepository;

@Service
@RequiredArgsConstructor
public class DoctorService {
	
	private final DoctorRepository doctorRepository;
		
	public List<Doctor> loadDoctors(List<String[]> data) {
		List<Doctor> doctors = new ArrayList<Doctor>();
		
		try {
			data.stream().forEach(row -> doctors.add(Doctor.builder()
										  		.id(Long.valueOf(row[0]))
										  		.lastName(row[1])
										  		.firstName(row[2])
										  		.specialty(row[3])
										  		.dateOfBirth(Date.valueOf(row[4]))
										  		.nip(row[5])
										  		.pesel(row[6])
										  		.build()));
		}
		catch (IndexOutOfBoundsException | NumberFormatException e) {
			throw new IncorrectEntityDataException(Doctor.class);
		}
		
		return doctorRepository.saveAll(doctors);
	}
	
	public List<String> exportDoctors() {
		return getAllDoctors().stream().map(d -> d.toString())
									   .collect(Collectors.toList());
	}
	
	public List<Doctor> getAllDoctors() {
		return doctorRepository.findAll();
	}
	
	public void deleteAllDoctors() {
		doctorRepository.deleteAll();
	}

	public Page<Doctor> getAllDoctors(Pageable pageable) {

		return doctorRepository.findAll(pageable);
	}
	
}
