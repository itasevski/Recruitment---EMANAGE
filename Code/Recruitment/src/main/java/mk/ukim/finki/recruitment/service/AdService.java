package mk.ukim.finki.recruitment.service;

import mk.ukim.finki.recruitment.model.Ad;
import mk.ukim.finki.recruitment.model.Company;

import java.util.List;

public interface AdService {

    List<Ad> getAllAds();

    List<Ad> getAdsByCompanyId(String id);

    List<Ad> getAdsByPersonUsername(String username);

    void save(String header, String body, Company company); // Company

    void delete(Long id);

    void save(String username, Long id); // Person

    void delete(String username, Long id);

}
