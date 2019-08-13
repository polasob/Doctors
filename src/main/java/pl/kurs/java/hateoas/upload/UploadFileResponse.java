package pl.kurs.java.hateoas.upload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {

	private String fileName;
    private String fileType;
    private long size;
}
