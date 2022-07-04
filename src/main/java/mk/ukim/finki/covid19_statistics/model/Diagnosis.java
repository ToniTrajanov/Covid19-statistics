package mk.ukim.finki.covid19_statistics.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

@Data
@Entity
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagnoseId;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Visit> visits;

    private String description;

    public void setVisits(Visit visit){
        this.visits.remove(visits.size()-1);
        this.visits.add(visit);
    }


    public Diagnosis()
    {

    }
    public String getDescription(){
        return description;
    }

    public Diagnosis(String name, Visit visit,  String description) {
        this.name = name;
        visits = new ArrayList<>();
        this.visits.add(visit);
        this.description = description;
    }
}
