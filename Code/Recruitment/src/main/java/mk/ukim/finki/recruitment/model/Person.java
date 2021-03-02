package mk.ukim.finki.recruitment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mk.ukim.finki.recruitment.model.enumerations.AccountStatus;
import mk.ukim.finki.recruitment.model.enumerations.Role;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "emanage_person")
public class Person extends User {

    @Id
    private String username;

    @ManyToMany
    private List<Ad> ads;

    public Person() {}

    public Person(String username, String email, String password, String name, Role role, String imageSourceUrl, String bio, String accountRole, AccountStatus accountStatus) {
        super(email, password, name, role, imageSourceUrl, bio, accountRole, accountStatus);
        this.username = username;
    }
}
