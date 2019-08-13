package pl.kurs.java.hateoas.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "visits" })
@EntityListeners(AuditingEntityListener.class)
public class Patient extends Auditable{

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String lastName;
	private String firstName;
	private String pesel;
	private Date dateOfBirth;
	
	@OneToMany(mappedBy="patient", cascade = CascadeType.ALL)
	private List<Visit> visits;
	
	@Override
	public String toString() {
		return id + ", " + lastName + ", " + firstName + ", "
			   + pesel + ", " + dateOfBirth;
	}
}
