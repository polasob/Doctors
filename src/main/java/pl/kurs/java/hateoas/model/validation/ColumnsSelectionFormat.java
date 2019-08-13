package pl.kurs.java.hateoas.model.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = ColumnsSelectionFormatValidator.class)
public @interface ColumnsSelectionFormat {

	String message() default "Columns format is incorrect !";
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    Class<?> entity();
}
