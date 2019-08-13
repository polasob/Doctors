package pl.kurs.java.hateoas.model.validation;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ColumnsSelectionFormatValidator implements ConstraintValidator<ColumnsSelectionFormat, String> {

	private Class<?> entity;
	private final EntityManager entityManager;
	
	public void initialize(ColumnsSelectionFormat constraintAnnotation) {
		entity = constraintAnnotation.entity();
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		
		Metamodel metamodel = entityManager.getMetamodel();
		EntityType<?> managedEntity = metamodel.entity(entity);
				
		String[] columns = value.split(",");
		for (String column : columns) {
			try {
				managedEntity.getAttribute(column);
			}
			catch(IllegalArgumentException ex) {
				return false;
			}
		}
		
		return true;
	}

}
