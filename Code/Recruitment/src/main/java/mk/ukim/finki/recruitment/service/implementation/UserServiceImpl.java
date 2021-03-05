package mk.ukim.finki.recruitment.service.implementation;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.AccountStatus;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.model.enumerations.UserType;
import mk.ukim.finki.recruitment.model.exceptions.*;
import mk.ukim.finki.recruitment.repository.CompanyRepository;
import mk.ukim.finki.recruitment.repository.PersonRepository;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private PasswordEncoder passwordEncoder;
    private CompanyRepository companyRepository;
    private PersonRepository personRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder, CompanyRepository companyRepository, PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
    }

    @Override
    public List<User> getAllUsers() {
        List<Company> companies = getCompanyProfiles();
        List<Person> people = getPersonProfiles();

        return Stream.concat(companies.stream(), people.stream()).collect(Collectors.toList());
    }

    @Override
    public List<Company> getCompanyProfiles() {
        return this.companyRepository.findAll();
    }

    @Override
    public List<Person> getPersonProfiles() {
        return this.personRepository.findAll();
    }

    @Override
    public List<User> getUsersByQueryString(String queryString) {
        List<Company> companies = this.companyRepository.findByNameContainingIgnoreCase(queryString);
        List<Person> people = this.personRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(queryString, queryString);

        return Stream.concat(companies.stream(), people.stream()).collect(Collectors.toList());
    }


    @Override
    public User register(String nameArg, String email, String password, String repeatedPassword, String userType) {
        if(nameArg == null || nameArg.isEmpty() ||
                email == null || email.isEmpty()) {
            throw new ArgumentsNotValidException();
        }
        else if(!password.equals(repeatedPassword)) throw new NonMatchingPasswordsException();

        emailCheck(email);

        if(userType.equals("person")) usernameCheck(nameArg);

        return userType.equals("person") ? this.personRepository.save(new Person(nameArg, email, this.passwordEncoder.encode(password), nameArg, Role.ROLE_USER, "../images/profilePictures/person-default.png", null, null, AccountStatus.ACTIVE, UserType.PERSON))
                : this.companyRepository.save(new Company(email, this.passwordEncoder.encode(password), nameArg, Role.ROLE_USER, "../images/profilePictures/company-default.png", null, null, AccountStatus.ACTIVE, UserType.COMPANY));
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> user = this.personRepository.findByUsername(s);
         return user.isPresent() ? user.get() : this.companyRepository.findByEmailOrId(s, s)
                 .orElseThrow(() -> new UserNotFoundException(s));
    }

    @Override
    public boolean findPersonByUsername(String username) {
        return this.personRepository.existsByUsername(username);
    }

    @Override
    public Company findCompanyById(String id) {
        return this.companyRepository.findById(id).get();
    }

    @Override
    public User getUserInstanceByUUID(String uuid) {
        Optional<Person> user = this.personRepository.findByUsername(uuid);
        return user.isPresent() ? user.get() : this.companyRepository.findById(uuid).get();
    }

    @Override
    public void update(String uuid, String name, String bio, String accountRole, MultipartFile profilePicture, String imageSourceUrl) {
        User user = getUserInstanceByUUID(uuid);

        if(name != null && !name.isEmpty()) user.setName(name);
        if(bio != null && !bio.isEmpty()) user.setBio(bio);
        if(accountRole != null && !accountRole.isEmpty()) user.setAccountRole(accountRole);
        if(!profilePicture.isEmpty()) user.setImageSourceUrl(imageSourceUrl);

        if (user instanceof Person) this.personRepository.save((Person) user);
        else this.companyRepository.save((Company) user);
    }

    @Override
    public void banUser(String uuid) {
        Optional<Person> user = this.personRepository.findByUsername(uuid);
        if (user.isPresent()) this.personRepository.deleteById(uuid);
        else this.companyRepository.deleteById(uuid);
    }

    @Override
    public long getActiveUsers() {
        return this.personRepository.findAll().stream()
                .filter(person -> person.getRole() == Role.ROLE_USER && person.getAccountStatus() == AccountStatus.ACTIVE)
                .count();
    }

    // === my methods === //

    public void emailCheck(String email) {
         if(this.personRepository.existsByEmail(email) || this.companyRepository.existsByEmail(email)) throw new EmailAlreadyAssociatedException(email);
    }
    // Eden email ne moze da bide asociran so povekje od eden account.

    public void usernameCheck(String username) {
        if(this.personRepository.existsByUsername(username)) throw new UsernameExistsException(username);
    }
    // Ne moze da ima dva isti usernames. (Integrity constraint)
}
