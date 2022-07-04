package mk.ukim.finki.covid19_statistics.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String therapy;

    @OneToOne
    Diagnosis diagnosis;

    public Receipt() {
    }

    public Receipt(String therapy, Diagnosis diagnosis) {
        this.therapy = therapy;
        this.diagnosis = diagnosis;
    }
}
