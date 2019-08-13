package pl.kurs.java.hateoas.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class VisitDto {

	private Long id;
	private DoctorDto doctor;
	private PatientDto patient;
	private Date date;
}
