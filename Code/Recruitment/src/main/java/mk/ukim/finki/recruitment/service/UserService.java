package mk.ukim.finki.recruitment.service;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.Person;
import mk.ukim.finki.recruitment.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    List<Company> getCompanyProfiles();

    List<Person> getPersonProfiles();

    List<User> getUsersByQueryString(String queryString);

    User register(String nameArg, String email, String password, String repeatedPassword, String userType);

    boolean findPersonByUsername(String username);

    Company findCompanyById(String id);

    User getUserInstanceByUUID(String uuid);

    void update(String uuid, String name, String bio, String accountRole, MultipartFile profilePicture, String imageSourceUrl);

    long getActiveUsers();

}
