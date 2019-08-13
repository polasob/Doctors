package pl.kurs.java.hateoas.model.dto;

import java.util.Date;

import lombok.Data;

@Data
public class DoctorDto {

	private Long id;
	private String lastName;
	private String firstName;
	private String specialty;
	private Date dateOfBirth;
	private String nip;
	private String pesel;
}
