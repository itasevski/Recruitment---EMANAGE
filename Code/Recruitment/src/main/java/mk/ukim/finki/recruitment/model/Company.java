package mk.ukim.finki.recruitment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mk.ukim.finki.recruitment.model.enumerations.AccountStatus;
import mk.ukim.finki.recruitment.model.enumerations.Role;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Ad> ads;

    public Company() {}

    public Company(String email, String password, String name, Role role, String imageSourceUrl, String bio, String accountRole, AccountStatus accountStatus) {
        super(email, password, name, role, imageSourceUrl, bio, accountRole, accountStatus);
    }

    @Override
    public String getUsername() {
        return this.id;
    }
}
