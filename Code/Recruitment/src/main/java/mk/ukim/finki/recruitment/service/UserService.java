package mk.ukim.finki.recruitment.service;

import mk.ukim.finki.recruitment.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(String nameArg, String email, String password, String repeatedPassword, String userType);

}
