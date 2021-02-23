package mk.ukim.finki.recruitment.configuration.security;

import mk.ukim.finki.recruitment.model.exceptions.ArgumentsNotValidException;
import mk.ukim.finki.recruitment.model.exceptions.UserNotFoundException;
import mk.ukim.finki.recruitment.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if(username.isEmpty() || password.isEmpty()) throw new ArgumentsNotValidException();

        UserDetails userDetails = null;

        try {
            userDetails = this.userService.loadUserByUsername(username);
        }
        catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }

        if(userDetails == null) throw new UsernameNotFoundException("User with given UUID doesn't exist.");

        if(!this.passwordEncoder.matches(password, userDetails.getPassword())) throw new BadCredentialsException("Incorrect password.");

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
