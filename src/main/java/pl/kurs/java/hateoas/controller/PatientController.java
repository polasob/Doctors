package pl.kurs.java.hateoas.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import pl.kurs.java.hateoas.model.Patient;
import pl.kurs.java.hateoas.model.dto.PatientDto;
import pl.kurs.java.hateoas.model.validation.ColumnsSelectionFormat;
import pl.kurs.java.hateoas.model.validation.TextFileFormat;
import pl.kurs.java.hateoas.service.FileStorageService;
import pl.kurs.java.hateoas.service.MyService;
import pl.kurs.java.hateoas.service.PatientService;
import pl.kurs.java.hateoas.upload.UploadFileResponse;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@Validated
public class PatientController {

	private final PatientService patientService;
	private final ModelMapper mapper;
	private final FileStorageService fileStorageService;
	private final MyService myService;

	@PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") @TextFileFormat(ignoreFirstLine = true,
    		regex="\\d+\t[a-zA-Z]+\t[a-zA-Z]+\t\\d{11}\t\\d{4}-\\d{1,2}-\\d{1,2}") MultipartFile file) {
        
		Path filePath = fileStorageService.storeFile(file);
    	String fileName = filePath.getFileName().toString();

        List<String[]> data = fileStorageService.loadDataFromFile(filePath.toString(), "\t");
        patientService.loadPatients(data);
        
        return new UploadFileResponse(fileName, file.getContentType(), file.getSize());
    }
	
	@GetMapping("/export/csv")
    public ResponseEntity<Resource> downloadAllDoctors(@RequestParam("columns") @Nullable 
    	@ColumnsSelectionFormat(entity=Patient.class) String columns, HttpServletRequest request) {
    	
		List<String> patients;
		if (columns == null) {
    		
    		patients = patientService.exportPatients();
    	}
    	else {
    		patients = myService.exportColumns(Patient.class, columns.split(","));
    		
    	}
		Path filePath = fileStorageService.loadDataToFile("patients.csv", patients);
        Resource resource = fileStorageService.loadFileAsResource(filePath);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } 
        catch (IOException ex) {
            
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	
	@GetMapping("/loadPatientsFromFile")
	public ResponseEntity<List<PatientDto>> loadPatientsFromFile() {
		List<String[]> data = fileStorageService.loadDataFromFile(
				"C:\\Users\\Paulina\\Downloads\\pacjenci.txt", "\t");
		List<Patient> patients = patientService.loadPatients(data);

		return ResponseEntity
				.ok(patients.stream().map(p -> mapper.map(p, PatientDto.class)).collect(Collectors.toList()));
	}

	@GetMapping("/")
	public ResponseEntity<Page<PatientDto>> getAllPatients(
			@PageableDefault(size = 10, page = 0, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(patientService.getAllPatients(pageable).map(p -> mapper.map(p, PatientDto.class)));
	}

	@DeleteMapping("/")
	public ResponseEntity<Void> deleteAllDoctors() {
		patientService.deleteAllPatients();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
