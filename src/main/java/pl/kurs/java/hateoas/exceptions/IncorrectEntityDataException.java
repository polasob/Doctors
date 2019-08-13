package pl.kurs.java.hateoas.exceptions;

public class IncorrectEntityDataException extends RuntimeException {

	private static final long serialVersionUID = -456982562531370116L;

	public IncorrectEntityDataException(Class<?> entity) {
		super("Incorrect data for entity " + entity);
	}
}
