package pl.kurs.java.hateoas.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PatientDto {

	private Long id;
	private String lastName;
	private String firstName;
	private String pesel;
	private Date dateOfBirth;
}
