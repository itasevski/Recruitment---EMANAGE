package mk.ukim.finki.recruitment.repository;

import mk.ukim.finki.recruitment.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    Optional<Person> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

}
