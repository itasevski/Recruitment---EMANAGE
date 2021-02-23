package mk.ukim.finki.recruitment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "emanage_company")
public class Company extends User {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private String id;

    private String name;


    public Company() {}

    public Company(String email, String password, Role role, String name) {
        super(email, password, role);
        this.name = name;
    }

    @Override
    public String getUsername() {
        return this.id;
    }
}
