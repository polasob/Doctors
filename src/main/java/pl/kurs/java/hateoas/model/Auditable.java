package pl.kurs.java.hateoas.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Data;

@Data
@MappedSuperclass
public class Auditable {
	@CreatedBy
	private String createdBy;
	@LastModifiedBy
	private String modifiedBy;
	@CreatedDate
	private Date createdDate;
	@LastModifiedDate
	private Date lastModifiedDate;
}
