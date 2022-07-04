package mk.ukim.finki.covid19_statistics.service;

import mk.ukim.finki.covid19_statistics.model.Diagnosis;
import mk.ukim.finki.covid19_statistics.model.Visit;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface DiagnosisService {

    List<Diagnosis> findAll();
    Diagnosis findByDiagnoseId(Long diagnoseId);
    List<Diagnosis> findByName(String name);
    Diagnosis edit(Long id, String name, LocalDateTime term, String description);
    Diagnosis findByVisit(Long patientSsn);
//    List<Diagnosis> findByVisit(Long patientSsn);
    Diagnosis create(String name, LocalDateTime term, String description);
    Diagnosis delete(Long id);
    List<Diagnosis> filter(String diagnoseName);
}
