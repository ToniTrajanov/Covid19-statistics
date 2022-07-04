package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Doctor {

    @Id
    private Long ssn;

    private String name;

    private String surname;

    private Integer licenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Specialty specialty;

    public String getNameAndSurname() {
        return name+ " "+surname;
    }

    public Long getSsn() {
        return ssn;
    }

    public Doctor() {
    }

    public Doctor(Long ssn, String name, String surname, Integer licenceNumber, Specialty specialty) {
        this.ssn = ssn;
        this.name = name;
        this.surname = surname;
        this.licenceNumber = licenceNumber;
        this.specialty = specialty;
    }
}
