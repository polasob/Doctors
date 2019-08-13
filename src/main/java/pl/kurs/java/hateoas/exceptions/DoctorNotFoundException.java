package pl.kurs.java.hateoas.exceptions;

public class DoctorNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4568894865093182060L;
	
	public DoctorNotFoundException(Long id) {
		super("Doctor with id " + id + " not found");
	}

}
