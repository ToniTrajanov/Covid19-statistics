package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime term;

    @ManyToOne(optional = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Patient patient;




    public Visit() {
    }

    public Visit(LocalDateTime term, Doctor doctor, Patient patient) {
        this.term = term;
        this.doctor = doctor;
        this.patient = patient;
    }
}
