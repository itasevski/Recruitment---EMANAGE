package mk.ukim.finki.recruitment.repository;

import mk.ukim.finki.recruitment.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    Optional<Company> findByEmailOrId(String email, String id);

    boolean existsByEmail(String email);

    boolean existsByIdOrEmail(String id, String email);

}
