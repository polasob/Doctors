package pl.kurs.java.hateoas.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import pl.kurs.java.hateoas.exceptions.FileStorageException;
import pl.kurs.java.hateoas.exceptions.MyFileNotFoundException;
import pl.kurs.java.hateoas.property.FileStorageProperties;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;
	private final Path fileDownloadLocation;
	private BigDecimal downloadedFilesCounter;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        this.fileDownloadLocation = Paths.get(fileStorageProperties.getDownloadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory "
            			+ "where the uploaded files will be stored.", ex);
        }
        try {
            Files.createDirectories(this.fileDownloadLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory "
            			+ "where new files for download will be stored.", ex);
        }
        downloadedFilesCounter = new BigDecimal(0);
    }

    public Path storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetFilePath = generateFilePathWithUUID(fileName, this.fileStorageLocation);
            Files.createDirectories(targetFilePath.getParent());
            Files.copy(file.getInputStream(), targetFilePath, StandardCopyOption.REPLACE_EXISTING);

            return targetFilePath;
        } 
        catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    private Path generateFilePathWithUUID(String fileName, Path storageLocation) {
    	String fileNameWithoutExt = getFileNameWithoutExt(fileName);
    	String fileExt = getFileExt(fileName);
    	String uuid = UUIDGenerator.generateType4UUID().toString();
		
		String fileNameWithUUID = fileNameWithoutExt + "_" + uuid + fileExt;
		return storageLocation.resolve(uuid).resolve(fileNameWithUUID);
    }
    
    private String getFileNameWithoutExt(String fileName) {
    	int pos = fileName.lastIndexOf(".");
    	if (pos > 0 && pos < (fileName.length() - 1)) {
    	    return fileName.substring(0, pos);
    	}
    	else {
    		throw new FileStorageException("Incorrect file name: " + fileName);
    	}
    }
    
    private String getFileExt(String fileName) {
    	int pos = fileName.lastIndexOf(".");
    	if (pos > 0 && pos < (fileName.length() - 1)) {
    	    return fileName.substring(pos, fileName.length());
    	}
    	else {
    		throw new FileStorageException("Incorrect file name: " + fileName);
    	}
    }

    public Resource loadFileAsResource(Path filePath) {
    	String fileName = filePath.getFileName().toString();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } 
            else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } 
        catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
        
    public List<String[]> loadDataFromFile(String filePath, String delimiter) {
		List<String[]> data = new ArrayList<String[]>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = null;
			line = br.readLine();
			while ((line = br.readLine()) != null) {
				data.add(line.split(delimiter));
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return data;
		
	}
	
	public Path loadDataToFile(String fileName, List<String> lines) {
		Charset utf8 = StandardCharsets.UTF_8;
		Path targetFilePath = generateFilePathWithUUID(fileName, this.fileDownloadLocation);
		
		try {
			Files.createDirectories(targetFilePath.getParent());
			return Files.write(targetFilePath, lines, utf8,
		            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} 
		catch (IOException e) {
			throw new FileStorageException(targetFilePath.toString());
		}
	}
}
