package pl.kurs.java.hateoas.model.validation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class TextFileFormatValidator implements ConstraintValidator<TextFileFormat, MultipartFile> {

	private String regex;
	private boolean ignoreFirstLine;
	
	public void initialize(TextFileFormat constraintAnnotation) {
		regex = constraintAnnotation.regex();
		ignoreFirstLine = constraintAnnotation.ignoreFirstLine();
	}
	
	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if(file.isEmpty()){
			return false;
        }
        if(!"text/plain".equalsIgnoreCase(file.getContentType())){
        	return false;
        }
       
        try(InputStream in = file.getInputStream()) {
        	InputStreamReader r = new InputStreamReader(in);
        	BufferedReader br = new BufferedReader(r);
        	String line = null;
        	if (ignoreFirstLine) {
        		line = br.readLine();
        	}
			while ((line = br.readLine()) != null) {
				if (!line.matches(regex)) {
					return false;
				}
			}
        }
        catch (IOException ex) {
        	
        }
		return true;
	}

}
