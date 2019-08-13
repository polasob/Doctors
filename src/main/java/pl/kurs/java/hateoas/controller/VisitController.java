package pl.kurs.java.hateoas.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
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

import lombok.RequiredArgsConstructor;
import pl.kurs.java.hateoas.model.Visit;
import pl.kurs.java.hateoas.model.dto.VisitDto;
import pl.kurs.java.hateoas.model.validation.ColumnsSelectionFormat;
import pl.kurs.java.hateoas.model.validation.TextFileFormat;
import pl.kurs.java.hateoas.service.FileStorageService;
import pl.kurs.java.hateoas.service.MyService;
import pl.kurs.java.hateoas.service.VisitService;
import pl.kurs.java.hateoas.upload.UploadFileResponse;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
@Validated
public class VisitController {

	private final VisitService visitService;
	private final ModelMapper mapper;
	private final FileStorageService fileStorageService;
	private final MyService myService;
	
	@PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") @TextFileFormat(ignoreFirstLine = true,
    		regex="\\d+\t\\d+\t\\d{4}-\\d{1,2}-\\d{1,2}") MultipartFile file) {
        
		Path filePath = fileStorageService.storeFile(file);
    	String fileName = filePath.getFileName().toString();

        List<String[]> data = fileStorageService.loadDataFromFile(filePath.toString(), "\t");
        visitService.loadVisits(data);
        
        return new UploadFileResponse(fileName, file.getContentType(), file.getSize());
    }
	
	@GetMapping("/export/csv")
    public ResponseEntity<Resource> downloadAllDoctors(@RequestParam("columns") @Nullable 
    	@ColumnsSelectionFormat(entity=Visit.class) String columns, HttpServletRequest request) {
    	
		List<String> visits;
    	if (columns == null) {
    		
    		visits = visitService.exportVisits();
    	}
    	else {
    		visits = myService.exportColumns(Visit.class, columns.split(","));
    		
    	}
    	Path filePath = fileStorageService.loadDataToFile("visits.csv", visits);
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
	
	@GetMapping("/loadVisitsFromFile")
	public ResponseEntity<List<VisitDto>> loadVisitsFromFile() {
		
		List<String[]> data = fileStorageService.loadDataFromFile(
				"C:\\Users\\Paulina\\Downloads\\wizyty.txt", "\t");
		List<Visit> visits = visitService.loadVisits(data);
		
		return ResponseEntity.ok(visits.stream()
									   .map(v -> mapper.map(v, VisitDto.class))
									   .collect(Collectors.toList()));
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<VisitDto>> getAllVisits(
			@PageableDefault(size = 10, page = 0, sort = "date", direction = Sort.Direction.ASC) Pageable pageable) {
		
		return ResponseEntity.ok(visitService.getAllVisits(pageable)
									   .map(v -> mapper.map(v, VisitDto.class)));
	}
	
	@DeleteMapping("/")
	public ResponseEntity<Void> deleteAllDoctors() {
		visitService.deleteAllVisits();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
