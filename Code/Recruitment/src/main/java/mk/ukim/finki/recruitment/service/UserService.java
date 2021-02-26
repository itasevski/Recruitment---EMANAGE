package mk.ukim.finki.recruitment.service;

import mk.ukim.finki.recruitment.model.Company;
import mk.ukim.finki.recruitment.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {

    User register(String nameArg, String email, String password, String repeatedPassword, String userType);

    boolean findPersonByUsername(String username);

    Company findCompanyById(String id);

    User getUserInstanceByUUID(String uuid);

}
