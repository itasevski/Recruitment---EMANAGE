package mk.ukim.finki.recruitment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mk.ukim.finki.recruitment.model.enumerations.Role;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "emanage_person")
public class Person extends User {

    @Id
    private String username;


    public Person() {}

    public Person(String email, String password, Role role, String imageSourceUrl, String username) {
        super(email, password, role, imageSourceUrl);
        this.username = username;
    }
}
