package pl.kurs.java.hateoas.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -4582398382616366846L;

	public MyFileNotFoundException(String message) {
		super(message);
	}
	
	public MyFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
