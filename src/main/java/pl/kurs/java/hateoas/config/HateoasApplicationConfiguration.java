package pl.kurs.java.hateoas.config;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.hateoas.model.validation.ColumnsSelectionFormatValidator;
import pl.kurs.java.hateoas.model.validation.TextFileFormatValidator;
import pl.kurs.java.hateoas.property.FileStorageProperties;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableConfigurationProperties({FileStorageProperties.class})
public class HateoasApplicationConfiguration {
	
	private final EntityManager entityManager;
	
	@Bean
	public ModelMapper mapper() {
		ModelMapper mapper = new ModelMapper();
		return mapper;
	}
	
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.ofNullable("System");
	}
	
	@Bean
	public TextFileFormatValidator textFileFormatValidator() {
	    return new TextFileFormatValidator();
	}
	
	@Bean
	public ColumnsSelectionFormatValidator columnsSelectionFormatValidator() {
	    return new ColumnsSelectionFormatValidator(entityManager);
	}
}
