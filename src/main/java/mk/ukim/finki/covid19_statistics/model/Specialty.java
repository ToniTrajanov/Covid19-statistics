package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Specialty() {
    }

    public Specialty(String name) {
        this.name = name;
    }
}
