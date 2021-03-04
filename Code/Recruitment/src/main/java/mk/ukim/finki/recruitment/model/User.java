package mk.ukim.finki.recruitment.model;

import lombok.Data;
import mk.ukim.finki.recruitment.model.enumerations.AccountStatus;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import mk.ukim.finki.recruitment.model.enumerations.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.util.Collection;
import java.util.Collections;


@Data
@MappedSuperclass
public abstract class User implements UserDetails {

    private String email;
    private String password;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String imageSourceUrl;
    private String bio;
    private String accountRole;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = true;

    public User() {}

    public User(String email, String password, String name, Role role, String imageSourceUrl, String bio, String accountRole, AccountStatus accountStatus, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.imageSourceUrl = imageSourceUrl;
        this.bio = bio;
        this.accountRole = accountRole;
        this.accountStatus = accountStatus;
        this.userType = userType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}
