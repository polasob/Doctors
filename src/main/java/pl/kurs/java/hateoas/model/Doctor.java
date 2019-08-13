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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "visits" })
@EntityListeners(AuditingEntityListener.class)
public class Doctor extends Auditable {

	@Id
	private Long id;
	private String lastName;
	private String firstName;
	private String specialty;
	private Date dateOfBirth;
	private String nip;
	private String pesel;

	@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
	private List<Visit> visits;
	
	@Override
	public String toString() {
		return id + ", " + lastName + ", " + firstName + ", "
			   + specialty + ", " + dateOfBirth + ", " + nip + ", " + pesel;
	}
}
