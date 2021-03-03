package mk.ukim.finki.recruitment.service.implementation;

import mk.ukim.finki.recruitment.model.Ad;
import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.exceptions.AdAlreadySavedException;
import mk.ukim.finki.recruitment.repository.AdRepository;
import mk.ukim.finki.recruitment.repository.PersonRepository;
import mk.ukim.finki.recruitment.service.AdService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private AdRepository adRepository;
    private PersonRepository personRepository;

    public AdServiceImpl(AdRepository adRepository, PersonRepository personRepository) {
        this.adRepository = adRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<Ad> getAllAds() {
        return this.adRepository.findAll().stream()
                .sorted(Ad.byDateOrIdComparatorNTO).collect(Collectors.toList());
    }

    @Override
    public List<Ad> getAdsByCompanyId(String id) {
        return this.adRepository.getAdsByCompany_Id(id).stream()
                .sorted(Ad.byDateOrIdComparatorNTO).collect(Collectors.toList());
    }

    @Override
    public List<Ad> getAdsByPersonUsername(String username) {
        return this.personRepository.findByUsername(username).get().getAds().stream()
                .sorted(Ad.byDateOrIdComparatorNTO).collect(Collectors.toList());
    }

    @Override
    public List<Ad> getSortedAds(String sortCriteria) {
        if(sortCriteria.equals("nto")) return getAllAds();
        else if(sortCriteria.equals("otn")) return this.adRepository.findAll().stream().sorted(Ad.byDateOrIdComparatorOTN).collect(Collectors.toList());
        else if(sortCriteria.equals("title")) return this.adRepository.findAll().stream().sorted(Ad.byHeaderOrIdComparator).collect(Collectors.toList());
        else return this.adRepository.findAll().stream().sorted(Ad.byCompanyNameOrIdComparator).collect(Collectors.toList());
    }

    @Override
    public List<Ad> getAdsByQueryString(String queryString) {
        return this.adRepository.findByTimestampContainingOrHeaderContainingIgnoreCaseOrCompany_NameContainingIgnoreCase(queryString, queryString, queryString);
    }

    @Override
    public void save(String header, String body, Company company) {
        this.adRepository.save(new Ad(header, body, company));
    }

    @Override
    public void delete(Long id) {
        this.adRepository.deleteById(id);
    }

    @Override
    public void save(String username, Long id) {
        Person person = this.personRepository.findByUsername(username).get();

        if(person.getAds().stream().anyMatch(ad -> ad.getId().equals(id))) throw new AdAlreadySavedException(id);

        Ad ad = this.adRepository.findById(id).get();

        person.getAds().add(ad);
        this.personRepository.save(person);
    }

    @Override
    public void delete(String username, Long id) {
        Person person = this.personRepository.findByUsername(username).get();
        person.getAds().removeIf(ad -> ad.getId().equals(id));
        this.personRepository.save(person);
    }

    @Override
    public Company getAdOwner(Long id) {
        return this.adRepository.findById(id).get().getCompany();
    }

    @Override
    public Ad findById(Long id) {
        return this.adRepository.findById(id).get();
    }

    @Override
    public void updateAd(Long id, String header, String body) {
        Ad ad = this.adRepository.findById(id).get();
        ad.setHeader(header);
        ad.setBody(body);
        this.adRepository.save(ad);
    }


}
