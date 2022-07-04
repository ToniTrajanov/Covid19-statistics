package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Patient {

    @Id
    private Long ssn;

    private String name;

    private String surname;

    private Long uhid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Doctor doctor;



    public Patient() {
    }
    public String getNameAndSurname() {
        return name+ " "+surname;
    }
    public Patient(Long ssn, String name, String surname, Long uhid, Doctor doctor) {
        this.name = name;
        this.surname = surname;
        this.ssn = ssn;
        this.uhid = uhid;
        this.doctor = doctor;
    }
}
