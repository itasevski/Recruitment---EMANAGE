package mk.ukim.finki.recruitment.service.implementation;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.model.exceptions.*;
import mk.ukim.finki.recruitment.repository.CompanyRepository;
import mk.ukim.finki.recruitment.repository.PersonRepository;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public User register(String nameArg, String email, String password, String repeatedPassword, String userType) {
        if(nameArg == null || nameArg.isEmpty() ||
                email == null || email.isEmpty()) {
            throw new ArgumentsNotValidException();
        }
        else if(!password.equals(repeatedPassword)) throw new NonMatchingPasswordsException();

        emailCheck(email);

        if(userType.equals("person")) usernameCheck(nameArg);

        return userType.equals("person") ? this.personRepository.save(new Person(email, this.passwordEncoder.encode(password), Role.ROLE_USER, nameArg))
                : this.companyRepository.save(new Company(email, this.passwordEncoder.encode(password), Role.ROLE_USER, nameArg));
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

    // == my methods ==

    public void emailCheck(String email) {
         if(this.personRepository.existsByEmail(email) || this.companyRepository.existsByEmail(email)) throw new EmailAlreadyAssociatedException(email);
    }
    // Eden email ne moze da bide asociran so povekje od eden account.

    public void usernameCheck(String username) {
        if(this.personRepository.existsByUsername(username)) throw new UsernameExistsException(username);
    }
    // Ne moze da ima dva isti usernames. (Integrity constraint)
}
