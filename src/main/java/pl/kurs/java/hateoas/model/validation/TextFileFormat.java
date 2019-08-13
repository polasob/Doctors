package pl.kurs.java.hateoas.model.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import pl.kurs.java.hateoas.model.validation.TextFileFormatValidator;

@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = TextFileFormatValidator.class)
public @interface TextFileFormat {

	String message() default "Text file format is incorrect !";
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    String regex();
    boolean ignoreFirstLine();
}
