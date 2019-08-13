package pl.kurs.java.hateoas.exceptions;

public class PatientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6621187329755398260L;

	public PatientNotFoundException(Long id) {
		super("Patient with id " + id + " not found");
	}

}
