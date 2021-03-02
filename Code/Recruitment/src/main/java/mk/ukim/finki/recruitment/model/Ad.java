package mk.ukim.finki.recruitment.model;

import lombok.Data;
import mk.ukim.finki.recruitment.RecruitmentApplication;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@Entity
@Table(name = "emanage_ad")
public class Ad {

    @Transient
    public static Comparator<Ad> byDateOrIdComparator = Comparator.comparing(Ad::getTimestampAsDate).reversed()
            .thenComparing(Ad::getId);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;
    private String body;
    private String timestamp;

    @ManyToOne
    private Company company;

    public Ad () {}

    public Ad(String header, String body, Company company) {
        this.header = header;
        this.body = body;

        LocalDateTime now = LocalDateTime.now();
        timestamp = now.format(RecruitmentApplication.formatter);

        this.company = company;
    }

    public LocalDateTime getTimestampAsDate() {
        return LocalDateTime.parse(this.getTimestamp(), RecruitmentApplication.formatter);
    }

}
