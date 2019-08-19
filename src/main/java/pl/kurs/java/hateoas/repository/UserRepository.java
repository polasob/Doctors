package pl.kurs.java.hateoas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import pl.kurs.java.hateoas.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	UserDetails findByUsername(String username);

}
