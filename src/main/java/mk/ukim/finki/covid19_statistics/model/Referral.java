package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime term;

    @ManyToOne(fetch = FetchType.LAZY)
    private Patient ssnPatient;
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor forwardBy;
    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor forwardTo;

    public Referral() {
    }

    public Referral(LocalDateTime term, Patient ssnPatient, Doctor forwardBy, Doctor forwardTo) {
        this.term = term;
        this.ssnPatient = ssnPatient;
        this.forwardBy = forwardBy;
        this.forwardTo = forwardTo;
    }
}
