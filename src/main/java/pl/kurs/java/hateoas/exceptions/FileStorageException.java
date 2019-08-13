package pl.kurs.java.hateoas.exceptions;

public class FileStorageException extends RuntimeException {

	private static final long serialVersionUID = -8195866608952895834L;
	
	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileStorageException(String message) {
		super(message);
	}
}
