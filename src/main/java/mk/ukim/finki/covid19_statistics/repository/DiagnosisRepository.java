package mk.ukim.finki.covid19_statistics.repository;

import mk.ukim.finki.covid19_statistics.model.Diagnosis;
import mk.ukim.finki.covid19_statistics.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    Diagnosis findByDiagnoseId(Long diagnoseId);
    Diagnosis findByVisits(Visit visit);
    List<Diagnosis> findByName(String name);
}