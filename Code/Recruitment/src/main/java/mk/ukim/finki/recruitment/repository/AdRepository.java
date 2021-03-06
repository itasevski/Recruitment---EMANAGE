package mk.ukim.finki.recruitment.repository;

import mk.ukim.finki.recruitment.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> getAdsByCompany_Id(String id);

    List<Ad> findByTimestampContainingOrHeaderContainingIgnoreCaseOrCompany_NameContainingIgnoreCase(String timestamp, String header, String companyName);

}
