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
import pl.kurs.java.hateoas.model.Doctor;
import pl.kurs.java.hateoas.model.dto.DoctorDto;
import pl.kurs.java.hateoas.model.validation.ColumnsSelectionFormat;
import pl.kurs.java.hateoas.model.validation.TextFileFormat;
import pl.kurs.java.hateoas.service.DoctorService;
import pl.kurs.java.hateoas.service.FileStorageService;
import pl.kurs.java.hateoas.service.MyService;
import pl.kurs.java.hateoas.upload.UploadFileResponse;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@Validated
public class DoctorController {
	
	private final DoctorService doctorService;
	private final ModelMapper mapper;
	private final FileStorageService fileStorageService;
	private final MyService myService;
    
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") @TextFileFormat(ignoreFirstLine = true,
    		regex="\\d+\t[a-zA-Z]+\t[a-zA-Z]+\t[a-zA-Z]+\t\\d{4}-\\d{2}-\\d{2}\t"
    				+ "\\d{3}-\\d{3}-\\d{2}-\\d{2}\t\\d{11}") MultipartFile file) {
        
    	Path filePath = fileStorageService.storeFile(file);
    	String fileName = filePath.getFileName().toString();

        List<String[]> data = fileStorageService.loadDataFromFile(filePath.toString(), "\t");
        doctorService.loadDoctors(data);
        
        return new UploadFileResponse(fileName, file.getContentType(), file.getSize());
    }
    
    @GetMapping("/export/csv")
    public ResponseEntity<Resource> downloadAllDoctors(@RequestParam("columns") @Nullable 
    		@ColumnsSelectionFormat(entity=Doctor.class) String columns, HttpServletRequest request) {
    	
    	List<String> doctors;
    	if (columns == null) {
    		
    		doctors = doctorService.exportDoctors();
    	}
    	else {
    		doctors = myService.exportColumns(Doctor.class, columns.split(","));
    		
    	}
    	Path filePath = fileStorageService.loadDataToFile("doctors.csv", doctors);
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
	
	@GetMapping("/loadDoctorsFromFile")
	public ResponseEntity<List<DoctorDto>> loadDoctorsFromFile() {
		List<String[]> data = fileStorageService.loadDataFromFile(
				"C:\\Users\\Paulina\\Downloads\\lekarze.txt", "\t");
		List<Doctor> doctors = doctorService.loadDoctors(data);
		
		return ResponseEntity.ok(doctors.stream()
										.map(d -> mapper.map(d, DoctorDto.class))
										.collect(Collectors.toList()));
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<DoctorDto>> getAllDoctors(
			@PageableDefault(size = 10, page = 0, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
				
		return ResponseEntity.ok(doctorService.getAllDoctors(pageable)
										.map(d -> mapper.map(d, DoctorDto.class)));
	}
	
	@DeleteMapping("/")
	public ResponseEntity<Void> deleteAllDoctors() {
		doctorService.deleteAllDoctors();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
}
